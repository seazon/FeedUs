package com.seazon.feedme.lib.summary

import com.seazon.feedme.lib.ai.gemini.GeminiApi
import com.seazon.feedme.lib.ai.gemini.GeminiException
import io.ktor.client.network.sockets.SocketTimeoutException

@Deprecated("")
object SummaryUtil {

    const val TYPE_GEMINI = "gemini"

    suspend fun summary(query: String, language: String, key: String, model: String, type: String): String? {
        return when (type) {
            TYPE_GEMINI -> {
                try {
                    val api = GeminiApi()
                    val result = api.summary(query, language, key, model)
                    result?.dst
                } catch (e: GeminiException) {
                    e.printStackTrace()
                    throw Exception("[gemini]summary error: [${e.code}]${e.message}")
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                    throw Exception("[gemini]summary error: socket timeout")
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw Exception("[gemini]summary error: request failed")
                }
            }

            else -> {
                throw Exception("summary error: unknown type: $type")
            }
        }
    }
}