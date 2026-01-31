package com.seazon.feedme.lib.ai

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.utils.format
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.json.Json

class GeneralAIApi {

    suspend fun text2Text(
        aiModel: AIModel,
        key: String,
        targetModel: String,
        prompt: String,
        query: String,
        language: String
    ): String? {
        val selectedConfig = AIGenerationConfig.getConfig(aiModel).copy(apiKey = key)
        val userPrompt = prompt.format(language, query)
        try {
            val response = generateText(selectedConfig, targetModel, userPrompt)
            val generatedText = extractGeneratedText2(response, aiModel)
            println("result：$generatedText")
            return generatedText.orEmpty()
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
        if (!config.modelList.contains(targetModel)) {
            throw IllegalArgumentException("model $targetModel not exist in ${config.aiModel}'s support list")
        }

        val realApiUrl = config.apiUrl.format(targetModel)

        val body = if (config.aiModel == AIModel.Gemini) {
            val requestBody = RequestBody(
                listOf(
                    Content(
                        listOf(
                            Part(userPrompt),
                        ),
                    ),
                ),
            )
            Json.encodeToString(requestBody).trimIndent()
        } else {
            val requestBody = Text2TextRequest(
                model = targetModel,
                messages = listOf(Message(role = "user", content = userPrompt)),
                max_tokens = config.maxTokens,
                temperature = 0.7
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

    private fun extractGeneratedText2(response: SimpleResponse, aiModel: AIModel): String? {
        when (aiModel) {
            AIModel.Gemini -> {
                val result: Result = response.convertBody()
                val generatedText = extractGeneratedText(result)
                return generatedText?.dst.orEmpty()
            }

            else -> {
                val result: Text2TextResponse = response.convertBody()
                return extractGeneratedText(result)
            }
        }
    }

    private fun extractGeneratedText(response: Text2TextResponse): String? {
        return when {
            response.error != null -> null
            response.choices.isNullOrEmpty() -> null
            else -> response.choices.first().message?.content
        }
    }

    private fun extractGeneratedText(response: Result?): Translation? {
        val text = when {
            response?.error != null -> null
            response?.candidates.isNullOrEmpty() -> null
            else -> response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }
        return if (text.isNullOrEmpty()) {
            null
        } else {
            toJson<Translation>(text.replace("```json\n", "").replace("\n```", ""))
        }
    }
}
