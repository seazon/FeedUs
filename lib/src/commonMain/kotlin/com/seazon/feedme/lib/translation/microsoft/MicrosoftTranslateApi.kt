package com.seazon.feedme.lib.translation.microsoft

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.utils.format
import io.ktor.client.call.body
import kotlinx.serialization.Serializable

class MicrosoftTranslateApi {
    companion object {
        const val url = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=%1\$s"
    }

    suspend fun translate(query: String, language: String, key: String): List<TranslationData>? {
        val url = url.format(language)
        return HttpManager.request(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = mapOf(
                "Ocp-Apim-Subscription-Key" to key,
                "Content-Type" to "application/json",
            ),
            body = """[{"Text":"$query"}]""",
        ).body()
    }
}

@Serializable
data class TranslationData(
    val translations: List<TransResult>?,
)

@Serializable
data class TransResult(
    val src: String?,
    val text: String?,
)

