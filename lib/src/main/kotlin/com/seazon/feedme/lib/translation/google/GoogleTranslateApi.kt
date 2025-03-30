package com.seazon.feedme.lib.translation.google

import com.seazon.feedme.lib.network.HttpManager
import io.ktor.client.call.body
import io.ktor.client.request.get

class GoogleTranslateApi {
    companion object {
        const val url = "https://translation.googleapis.com/language/translate/v2?q=%1\$s&target=%2\$s&format=text&key=%3\$s"
    }

    suspend fun translate(query: String, language: String, key: String): String? {
        val u = url.format(query, language, key)
        return HttpManager.client.get(u).body()
    }
}
