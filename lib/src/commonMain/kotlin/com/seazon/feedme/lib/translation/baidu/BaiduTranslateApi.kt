package com.seazon.feedme.lib.translation.baidu

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.platform.Crypto
import io.ktor.client.call.body
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class BaiduTranslateApi {
    companion object {
        const val salt = "2025"
        const val url = "https://fanyi-api.baidu.com/api/trans/vip/translate"
    }

    suspend fun translate(query: String, language: String, key: String, appID: String): Translation? {
        val bytes = "$appID$query$salt$key"
        val sign = Crypto.md5(bytes)
        return HttpManager.request(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = mapOf(
                "Content-Type" to "application/x-www-form-urlencoded",
            ),
            params = listOf(
                NameValuePair("q", query),
                NameValuePair("from", "auto"),
                NameValuePair("to", language),
                NameValuePair("appid", appID),
                NameValuePair("salt", salt),
                NameValuePair("sign", sign),
            ),
        ).body()
    }
}

@Serializable
data class Translation(
    val from: String? = null,
    val to: String? = null,
    @SerialName("trans_result")
    val transResult: List<TransResult>? = null,
    @SerialName("error_code")
    val errorCode: String? = null,
    @SerialName("error_msg")
    val errorMsg: String? = null,
)

@Serializable
data class TransResult(
    val src: String? = null,
    val dst: String? = null,
)
