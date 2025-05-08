package com.seazon.feedme.lib.summary

import com.seazon.feedme.lib.ai.gemini.GeminiApi

object SummaryUtil {

    const val TYPE_GEMINI = "gemini"

    suspend fun summary(query: String, language: String, key: String, type: String): String? {
        return when (type) {
            TYPE_GEMINI -> {
                try {
                    val api = GeminiApi()
                    val result = api.summary(query, language, key)
                    result?.dst
                } catch (e: Exception) {
                    throw Exception("[gemini]summary error: ${e.message}, query: ${query.take(50)}")
                }
            }

            else -> {
                throw Exception("summary error: unknown type: $type")
            }
        }
    }
}