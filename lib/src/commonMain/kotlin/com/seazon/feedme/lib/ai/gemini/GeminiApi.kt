package com.seazon.feedme.lib.ai.gemini

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.translation.google.GoogleTranslateApi
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// ================== 接口定义 ==================

/**
 * 统一翻译/摘要服务接口
 */
interface ITranslationService {
    suspend fun translate(query: String, language: String): Translation?
    suspend fun summary(query: String, language: String): Translation?
}

// ================== 枚举与配置 ==================

/**
 * AI 提供商枚举
 */
enum class AiProvider(val defaultBaseUrl: String, val defaultModel: String) {
    GOOGLE_GEMINI(
        "https://generativelanguage.googleapis.com/v1beta/models/",
        "gemini-1.5-flash"
    ),
    OPENAI(
        "https://api.openai.com/v1/",
        "gpt-4o-mini"
    ),
    OPENROUTER(
        "https://openrouter.ai/api/v1/",
        "google/gemini-flash-1.5"
    ),
    ALI_BAILIAN(
        "https://dashscope.aliyuncs.com/compatible-mode/v1/",
        "qwen-plus"
    ),
    ZHIPU_GLM(
        "https://open.bigmodel.cn/api/paas/v4/",
        "glm-4-flash"
    ),
    SILICON_FLOW(
        "https://api.siliconflow.cn/v1/",
        "Qwen/Qwen2.5-7B-Instruct"
    ),
    // 传统 Google 翻译 (非 LLM)
    GOOGLE_TRANSLATE_V2(
        "https://translation.googleapis.com/language/translate/v2",
        ""
    );
}

/**
 * AI 配置类
 */
data class AiConfig(
    val provider: AiProvider,
    val apiKey: String,
    val baseUrl: String? = null,
    val model: String? = null
) {
    fun getEffectiveUrl(): String {
        // 如果是 Google 传统翻译，直接返回 BaseURL (参数通过 Query 拼接)
        if (provider == AiProvider.GOOGLE_TRANSLATE_V2) {
            return baseUrl ?: provider.defaultBaseUrl
        }

        var url = (baseUrl ?: provider.defaultBaseUrl)
        if (!url.endsWith("/")) url += "/"

        return if (provider == AiProvider.GOOGLE_GEMINI) {
            // Gemini 需要拼接 :generateContent
            val modelName = model ?: provider.defaultModel
            "${url}${modelName}:generateContent"
        } else {
            // OpenAI 兼容接口通常是 chat/completions
            "${url}chat/completions"
        }
    }

    fun getEffectiveModel(): String {
        return model ?: provider.defaultModel
    }
}

// ================== 工厂类 ==================

object TranslationServiceFactory {
    /**
     * 根据配置创建对应的服务实例
     */
    fun create(config: AiConfig): ITranslationService {
        return if (config.provider == AiProvider.GOOGLE_TRANSLATE_V2) {
            // 需要确保 GoogleTranslateApi 已实现了 ITranslationService 接口
            GoogleTranslateApi(config.apiKey)
        } else {
            AiService(config)
        }
    }
}

// ================== AI 服务实现核心 ==================

class AiService(private val config: AiConfig) : ITranslationService {

    private val jsonEncoder = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false // 避免发送值为 null 的字段
    }

    override suspend fun translate(query: String, language: String): Translation? {
        val prompt = "Translate to $language, JSON format output, key is dst. Text: $query"
        return executeRequest(prompt)
    }

    override suspend fun summary(query: String, language: String): Translation? {
        val prompt = "Summary the text in $language, JSON format output, key is dst, dst should be a string, and no more than 400 words, use markdown to improve readability if need. Text: $query"
        return executeRequest(prompt)
    }

    private suspend fun executeRequest(prompt: String): Translation? {
        if (config.apiKey.isEmpty()) {
            throw AiException(-1, "API key is required")
        }

        val rawJson: String? = if (config.provider == AiProvider.GOOGLE_GEMINI) {
            requestGemini(prompt)
        } else {
            requestOpenAiCompatible(prompt)
        }

        return if (rawJson.isNullOrEmpty()) {
            null
        } else {
            // 清理可能存在的 Markdown 代码块标记
            val cleanJson = rawJson
                .replace(Regex("^```json\\s*", RegexOption.MULTILINE), "")
                .replace(Regex("^```\\s*", RegexOption.MULTILINE), "")
                .trim()

            try {
                toJson<Translation>(cleanJson)
            } catch (e: Exception) {
                // 容错处理：尝试提取 JSON 子串
                val jsonStart = cleanJson.indexOf("{")
                val jsonEnd = cleanJson.lastIndexOf("}")
                if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                    try {
                        toJson<Translation>(cleanJson.substring(jsonStart, jsonEnd + 1))
                    } catch (e2: Exception) {
                        null
                    }
                } else {
                    null
                }
            }
        }
    }

    // --- Google Gemini 原生请求 ---
    private suspend fun requestGemini(prompt: String): String? {
        val requestBody = GeminiRequestBody(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt))))
        )
        val bodyStr = jsonEncoder.encodeToString(requestBody)

        val response: GeminiResult? = HttpManager.requestWrap(
            httpMethod = HttpMethod.POST,
            url = config.getEffectiveUrl(),
            headers = mapOf(
                HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON,
            ),
            params = listOf(
                NameValuePair("key", config.apiKey),
            ),
            body = bodyStr,
        ).convertBody()

        if (response?.error != null) {
            throw AiException(response.error.code.orZero(), response.error.message.orEmpty())
        }

        return response?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
    }

    // --- OpenAI 兼容接口请求 ---
    private suspend fun requestOpenAiCompatible(prompt: String): String? {
        val requestBody = OpenAiRequestBody(
            model = config.getEffectiveModel(),
            messages = listOf(
                OpenAiMessage(role = "system", content = "You are a helpful assistant. Always output valid JSON."),
                OpenAiMessage(role = "user", content = prompt)
            ),
            // 大部分兼容接口不需要 explicit response_format，Prompt 已经足够。
            // 填 null 以防某些模型(如 SiliconFlow 上的 Qwen)报错。
            responseFormat = null 
        )

        val bodyStr = jsonEncoder.encodeToString(requestBody)

        val response: OpenAiResult? = HttpManager.requestWrap(
            httpMethod = HttpMethod.POST,
            url = config.getEffectiveUrl(),
            headers = mapOf(
                HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON,
                "Authorization" to "Bearer ${config.apiKey}"
            ),
            body = bodyStr,
        ).convertBody()

        if (response?.error != null) {
            // OpenAI 错误通常在 message 中
            throw AiException(0, "${response.error.type}: ${response.error.message}")
        }

        return response?.choices?.firstOrNull()?.message?.content
    }
}

class AiException(val code: Int, message: String) : Exception(message)

// ================== 通用数据结构 ==================

@Serializable
data class Translation(
    val dst: String? = null,
)

// ================== Google Gemini 数据结构 ==================

@Serializable
data class GeminiRequestBody(val contents: List<GeminiContent>? = null)

@Serializable
data class GeminiContent(val parts: List<GeminiPart>? = null)

@Serializable
data class GeminiPart(val text: String? = null)

@Serializable
data class GeminiResult(
    val candidates: List<GeminiCandidates>? = null,
    val error: GeminiError? = null
) {
    @Serializable
    data class GeminiError(val code: Int? = null, val message: String? = null)
}

@Serializable
data class GeminiCandidates(val content: GeminiContent? = null)

// ================== OpenAI 兼容数据结构 ==================

@Serializable
data class OpenAiRequestBody(
    val model: String,
    val messages: List<OpenAiMessage>,
    val stream: Boolean = false,
    // 使用 @SerialName 处理 snake_case，避免自定义 Serializer
    // 设为 null 以兼容不支持 json_object 模式的模型
    @SerialName("response_format")
    val responseFormat: OpenAiResponseFormat? = null
)

@Serializable
data class OpenAiMessage(
    val role: String,
    val content: String
)

@Serializable
data class OpenAiResponseFormat(
    val type: String
)

@Serializable
data class OpenAiResult(
    val choices: List<OpenAiChoice>? = null,
    val error: OpenAiError? = null
) {
    @Serializable
    data class OpenAiError(
        val message: String? = null,
        val type: String? = null,
        val code: String? = null
    )
}

@Serializable
data class OpenAiChoice(
    val message: OpenAiMessage? = null
)
