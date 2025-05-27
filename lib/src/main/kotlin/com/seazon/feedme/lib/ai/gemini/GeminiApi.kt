package com.seazon.feedme.lib.ai.gemini

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.toType
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GeminiApi {

    companion object {
        const val URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
    }

    suspend fun translate(query: String, language: String, key: String): Translation? {
        val result = generateContent("Translate to $language, JSON format output, key is dst. Text: $query", key)
        val text = result?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        return if (text.isNullOrEmpty()) {
            null
        } else {
            toJson<Translation>(text.replace("```json\n", "").replace("```", ""))
        }
    }

    suspend fun summary(query: String, language: String, key: String): Translation? {
        val result = generateContent("Summary the text in $language, JSON format output, key is dst, dst should be a string, no more than 500 words, use markdown to improve readability if need. Text: $query", key)
        val text = result?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        return if (text.isNullOrEmpty()) {
            null
        } else {
            toJson<Translation>(text.replace("```json\n", "").replace("\n```", ""))
        }
    }

    private suspend fun generateContent(prompt: String, key: String): Result? {
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
        return HttpManager.request(
            httpMethod = HttpMethod.POST,
            url = URL,
            headers = mapOf(
                "Content-Type" to "application/json",
            ),
            params = listOf(
                NameValuePair("key", key),
            ),
            body = body,
        ).toType()
    }
}

@Serializable
data class RequestBody(
    val contents: List<Content>? = null,
)

@Serializable
data class Result(
    val candidates: List<Candidates>? = null,
)

@Serializable
data class Candidates(
    val content: Content? = null,
)

@Serializable
data class Content(
    val parts: List<Part>? = null,
)

@Serializable
data class Part(
    val text: String? = null,
)

@Serializable
data class Translation(
    val dst: String? = null,
)