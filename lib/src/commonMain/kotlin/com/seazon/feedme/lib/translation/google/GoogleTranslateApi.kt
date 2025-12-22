package com.seazon.feedme.lib.translation.google

import com.seazon.feedme.lib.ai.gemini.ITranslationService
import com.seazon.feedme.lib.ai.gemini.Translation
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.utils.toJson
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

/**
 * Google 传统翻译 API V2 实现
 * 实现了 ITranslationService 接口以配合多平台切换
 */
class GoogleTranslateApi(private val apiKey: String) : ITranslationService {

    companion object {
        private const val BASE_URL = "https://translation.googleapis.com/language/translate/v2"
    }

    override suspend fun translate(query: String, language: String): Translation? {
        if (apiKey.isEmpty()) return null

        try {
            // 使用 Ktor 的 parameter 构建器来自动处理 URL 编码，比 String.format 更安全
            val responseString: String = HttpManager.client.get(BASE_URL) {
                parameter("q", query)
                parameter("target", language)
                parameter("format", "text")
                parameter("key", apiKey)
            }.body()

            // 解析 Google 特有的 JSON 结构
            val googleResponse = toJson<GoogleTranslateResponse>(responseString)
            
            // 提取翻译文本并转换为通用的 Translation 对象
            val translatedText = googleResponse?.data?.translations?.firstOrNull()?.translatedText
            
            return if (!translatedText.isNullOrEmpty()) {
                // HTML 实体解码（Google 翻译有时会返回 &#39; 等），如果需要可以使用 Html.fromHtml
                // 这里直接返回 dst
                Translation(dst = translatedText)
            } else {
                null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // 传统翻译不支持摘要，返回 null 即可
    override suspend fun summary(query: String, language: String): Translation? {
        return null
    }
}

// ================== Google Translate V2 数据结构 ==================

@Serializable
private data class GoogleTranslateResponse(
    val data: GoogleTranslateData? = null
)

@Serializable
private data class GoogleTranslateData(
    val translations: List<GoogleTranslateItem>? = null
)

@Serializable
private data class GoogleTranslateItem(
    val translatedText: String? = null,
    val detectedSourceLanguage: String? = null
)
