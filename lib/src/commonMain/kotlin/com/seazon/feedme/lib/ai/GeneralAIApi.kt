package com.seazon.feedme.lib.ai

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.utils.format
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
