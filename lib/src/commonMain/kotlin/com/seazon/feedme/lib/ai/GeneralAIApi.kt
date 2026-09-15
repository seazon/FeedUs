package com.seazon.feedme.lib.ai

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.utils.LogUtils
import com.seazon.feedme.lib.utils.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class GeneralAIApi {

    suspend fun text2Text(
        aiModel: AIModel,
        baseUrl: String,
        key: String,
        targetModel: String,
        prompt: String,
        query: String,
        language: String
    ): String? {
        val selectedConfig = AIGenerationConfig.getConfig(aiModel).copy(apiUrl = baseUrl, apiKey = key)
        val userPrompt = prompt.replace("{content}", query)
            .replace("{language}", language)
        try {
            val response = generateText(selectedConfig, targetModel, userPrompt)
            val generatedText = extractGeneratedText(response, aiModel)
            println("result：$generatedText")
            return generatedText.orEmpty()
        } catch (e: AiException) {
            throw e
        } catch (e: Exception) {
            println("failed：${e.message}")
            e.printStackTrace()
            return null
        }
    }

    /**
     * Streams the generated text, emitting each incremental chunk.
     */
    fun text2TextStream(
        aiModel: AIModel,
        baseUrl: String,
        key: String,
        targetModel: String,
        prompt: String,
        query: String,
        language: String,
        maxTokens: Int = 2048,
    ): Flow<String> {
        val isGemini = aiModel == AIModel.Gemini
        val selectedConfig = AIGenerationConfig.getConfig(aiModel).copy(apiUrl = baseUrl, apiKey = key)
        val userPrompt = prompt.replace("{content}", query)
            .replace("{language}", language)
        val body = if (isGemini) {
            Json.encodeToString(
                GeminiStreamRequest(
                    contents = listOf(Content(parts = listOf(Part(text = userPrompt)))),
                    generationConfig = GeminiGenerationConfig(maxOutputTokens = maxTokens),
                )
            )
        } else {
            Json.encodeToString(
                GeneralAIStreamRequest(
                    model = targetModel,
                    messages = listOf(Message(role = "user", content = userPrompt)),
                    enableThinking = false,
                    stream = true,
                    maxTokens = maxTokens,
                )
            )
        }
        return HttpManager.requestStream(
            httpMethod = HttpMethod.POST,
            url = buildStreamUrl(selectedConfig.apiUrl.format(targetModel), isGemini),
            headers = buildMap {
                put(HttpUtils.HTTP_HEADERS_CONTENT_TYPE, HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON)
                if (!isGemini) {
                    put("Authorization", "Bearer ${selectedConfig.apiKey}")
                }
            },
            params = buildList {
                if (isGemini) {
                    add(NameValuePair("key", selectedConfig.apiKey))
                    add(NameValuePair("alt", "sse"))
                }
            },
            body = body,
        ).mapNotNull { line ->
            val data = extractSseData(line) ?: return@mapNotNull null
            try {
                extractStreamText(data, aiModel)?.takeIf { it.isNotEmpty() }
            } catch (e: SerializationException) {
                // Skip chunks that cannot be parsed, e.g. keep-alive or vendor specific payloads
                LogUtils.debug("skip chunk: $data, error: ${e.message}")
                null
            }
        }.catch { e ->
            if (e is AiException) throw e
            println("stream failed：${e.message}")
            throw AiException(message = e.message, cause = e)
        }
    }

    /**
     * Gemini needs streamGenerateContent instead of generateContent for streaming.
     */
    private fun buildStreamUrl(url: String, isGemini: Boolean): String {
        if (!isGemini) return url
        return if (url.contains(":generateContent")) {
            url.replace(":generateContent", ":streamGenerateContent")
        } else {
            url
        }
    }

    /**
     * Extracts the data payload from an SSE line, null means the line is not a data line.
     */
    private fun extractSseData(line: String): String? {
        val trimmed = line.trim()
        val data = when {
            trimmed.startsWith("data:") -> trimmed.removePrefix("data:").trim()
            trimmed.startsWith("{") -> trimmed
            else -> return null
        }
        if (data.isEmpty() || data == "[DONE]") return null
        return data
    }

    private fun extractStreamText(data: String, aiModel: AIModel): String? {
        return if (aiModel == AIModel.Gemini) {
            val result = Static.defaultJson.decodeFromString<GeminiResponse>(data)
            if (result.error != null) throw AiException(message = "code: ${result.error.code}, message: ${result.error.message}")
            result.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        } else {
            val result = Static.defaultJson.decodeFromString<GeneralAIResponse>(data)
            if (result.error != null) throw AiException(message = "code: ${result.error.code}, message: ${result.error.message}")
            result.choices?.firstOrNull()?.let { it.delta?.content ?: it.message?.content }
        }
    }

    private suspend fun generateText(
        config: AIGenerationConfig,
        targetModel: String,
        userPrompt: String
    ): SimpleResponse {
        val realApiUrl = config.apiUrl.format(targetModel)
        val body = if (config.aiModel == AIModel.Gemini) {
            val requestBody = GeminiRequest(
                contents = listOf(Content(parts = listOf(Part(text = userPrompt)))),
            )
            Json.encodeToString(requestBody).trimIndent()
        } else {
            val requestBody = GeneralAIRequest(
                model = targetModel,
                messages = listOf(Message(role = "user", content = userPrompt)),
                enableThinking = false,
            )
            Json.encodeToString(requestBody).trimIndent()
        }
        val response = HttpManager.requestWrap(
            httpMethod = HttpMethod.POST,
            url = realApiUrl,
            headers = buildMap {
                put(HttpUtils.HTTP_HEADERS_CONTENT_TYPE, HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON)
                if (config.aiModel != AIModel.Gemini) {
                    put("Authorization", "Bearer ${config.apiKey}")
                }
            },
            params = buildList {
                if (config.aiModel == AIModel.Gemini) {
                    add(NameValuePair("key", config.apiKey))
                }
            },
            body = body,
        )
        return response
    }

    private fun extractGeneratedText(response: SimpleResponse, aiModel: AIModel): String? {
        when (aiModel) {
            AIModel.Gemini -> {
                val result: GeminiResponse = response.convertBody()
                return extractGeneratedTextInner(result)
            }

            else -> {
                val result: GeneralAIResponse = response.convertBody()
                return extractGeneratedTextInner(result)
            }
        }
    }

    private fun extractGeneratedTextInner(response: GeneralAIResponse): String? {
        return when {
            response.error != null -> throw AiException(message = "code: ${response.error.code}, message: ${response.error.message}")
            response.choices.isNullOrEmpty() -> null
            else -> response.choices.first().message?.content
        }
    }

    private fun extractGeneratedTextInner(response: GeminiResponse?): String? {
        return when {
            response?.error != null -> throw AiException(message = "code: ${response.error.code}, message: ${response.error.message}")
            response?.candidates.isNullOrEmpty() -> null
            else -> response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }
    }

    suspend fun test(
        aiModel: AIModel,
        baseUrl: String,
        key: String,
        targetModel: String,
    ): String? {
        return GeneralAIApi().text2Text(aiModel, baseUrl, key, targetModel, "just return `test pass`", "", "")
    }
}
