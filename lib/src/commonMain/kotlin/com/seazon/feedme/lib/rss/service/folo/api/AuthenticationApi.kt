package com.seazon.feedme.lib.rss.service.folo.api

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.toType
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.folo.FoloConstants
import com.seazon.feedme.lib.rss.service.folo.bo.OneTimeTokenRequest
import com.seazon.feedme.lib.rss.service.folo.bo.OneTimeTokenResponse
import com.seazon.feedme.platform.TimeProvider
import io.ktor.http.setCookie
import kotlinx.serialization.json.Json

class AuthenticationApi : BaseApi() {

    fun getOAuth2Url(state: String?): String {
        return "https://app.folo.is/login"
    }

    suspend fun applyOneTimeTokenAndSetUser(rssToken: RssToken, token: String?) {
        val headers = mutableMapOf(
            HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON
        )
        val body = Json.encodeToString(OneTimeTokenRequest(token.orEmpty())).trimIndent()
        val rsp =
            HttpManager.request(
                httpMethod = HttpMethod.POST,
                url = getSchema() + FoloConstants.TOKEN,
                headers = headers,
                body = body
            )
        val accessToken =
            rsp.setCookie().firstOrNull { it.name == "__Secure-better-auth.session_token" }
        val response2 = rsp.toType<OneTimeTokenResponse>()

        rssToken.id = response2?.user?.id
        rssToken.refreshToken = token
        rssToken.accessToken = accessToken?.value
        rssToken.email = response2?.user?.email
        rssToken.picture = response2?.user?.image
        rssToken.expiresTimestamp = TimeProvider.currentTimeMillis() + 1000L * 3600 * 24 * 365 * 99
    }

    fun setUserWithAccessToken(token: RssToken, response: String) {
    }

}