package com.seazon.feedme.lib.translation

import com.seazon.feedme.lib.ai.gemini.GeminiApi
import com.seazon.feedme.lib.translation.baidu.BaiduTranslateApi
import com.seazon.feedme.lib.translation.google.GoogleTranslateApi
import com.seazon.feedme.lib.translation.microsoft.MicrosoftTranslateApi

object TranslatorUtil {

    const val TYPE_BAIDU = "baidu"
    const val TYPE_GOOGLE = "google"
    const val TYPE_MICROSOFT = "microsoft"
    const val TYPE_GEMINI = "gemini"

    suspend fun translate(query: String, language: String, key: String, appID: String, type: String): String? {
        return when (type) {
            TYPE_BAIDU -> {
                val api = BaiduTranslateApi()
                val result = api.translate(query, language, key, appID)
                if (result?.errorCode.isNullOrEmpty()) {
                    result?.transResult?.firstOrNull()?.dst
                } else {
                    throw Exception("[baidu]translate error code: ${result.errorCode}, error msg: ${result.errorMsg}, query: $query")
                }
            }

            TYPE_GEMINI -> {
                try {
                    val api = GeminiApi()
                    val result = api.translate(query, language, key)
                    result?.dst
                } catch (e: Exception) {
                    throw Exception("[gemini]translate error: ${e.message}, query: $query")
                }
            }

            TYPE_GOOGLE -> {
                val api = GoogleTranslateApi()
                api.translate(query, language, key)
            }

            TYPE_MICROSOFT -> {
                val api = MicrosoftTranslateApi()
                val result = api.translate(query, language, key)
                result?.firstOrNull()?.translations?.firstOrNull()?.text
            }

            else -> {
                throw Exception("translate error: unknown type: $type")
            }
        }
    }
}