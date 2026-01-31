package com.seazon.feedme.lib.ai.gemini

import com.seazon.feedme.lib.ai.Content
import com.seazon.feedme.lib.ai.Part
import com.seazon.feedme.lib.ai.RequestBody
import com.seazon.feedme.lib.ai.Result
import com.seazon.feedme.lib.ai.Translation
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.utils.format
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.json.Json

@Deprecated("Use GeneralAIApi")
class GeminiApi {

    companion object {
        const val URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent"

        val MODELS = listOf(
            "gemini-2.5-flash",
            "gemini-2.5-flash-lite",
            "gemini-2.5-pro",
            "gemini-3-flash-preview",
            "gemini-3-pro-preview",
        )
    }

    suspend fun translate(query: String, language: String, key: String, model: String): Translation? {
        val result = generateContent("Translate to $language, JSON format output, key is dst. Text: $query", key, model)
        val text = result?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        return if (text.isNullOrEmpty()) {
            null
        } else {
            toJson<Translation>(text.replace("```json\n", "").replace("```", ""))
        }
    }

    suspend fun summary(query: String, language: String, key: String, model: String): Translation? {
        val result = generateContent(
            prompt = "Summary the text in $language, JSON format output, key is dst, dst should be a string, and no more than 400 words, use markdown to improve readability if need. Text: $query",
            key = key,
            model = model,
        )
        val text = result?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        return if (text.isNullOrEmpty()) {
            null
        } else {
            toJson<Translation>(text.replace("```json\n", "").replace("\n```", ""))
        }
    }

    private suspend fun generateContent(prompt: String, key: String, model: String): Result? {
        if (key.isEmpty()) {
            throw GeminiException(-1, "API key is required for Gemini API")
        }
        if (model.isEmpty()) {
            throw GeminiException(-1, "Model is required for Gemini API")
        }
        val requestBody = RequestBody(
            listOf(
                Content(
                    listOf(
                        Part(prompt),
                    ),
                ),
            ),
        )
        val body = Json.encodeToString(requestBody).trimIndent()
        val result: Result? = HttpManager.requestWrap(
            httpMethod = HttpMethod.POST,
            url = URL.format(model),
            headers = mapOf(
                HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON,
            ),
            params = listOf(
                NameValuePair("key", key),
            ),
            body = body,
        ).convertBody()
        if (result?.error != null) {
            throw GeminiException(result.error.code.orZero(), result.error.message.orEmpty())
        }
        return result
    }
}

class GeminiException(val code: Int, message: String) : Exception(message)
