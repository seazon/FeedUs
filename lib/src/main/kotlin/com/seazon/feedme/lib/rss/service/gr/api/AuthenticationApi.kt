package com.seazon.feedme.lib.rss.service.gr.api

import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.Oauth2Response
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.gr.GrConfig
import com.seazon.feedme.lib.rss.service.gr.GrConstants
import com.seazon.feedme.lib.utils.orZero
import io.ktor.client.call.body

open class AuthenticationApi(token: RssToken, config: GrConfig) : BaseApi(token, config) {

    open suspend fun getAccessToken(username: String?, password: String?): String? {
        val parameters = listOf(
            NameValuePair("Email", username.orEmpty()),
            NameValuePair("Passwd", password.orEmpty()),
        )

        val headers = HashMap<String, String>()
        if (isTheseRssType(Static.ACCOUNT_TYPE_INOREADER_OAUTH2, Static.ACCOUNT_TYPE_INOREADER)) {
            setHeaderAppAuthentication(headers)
        }

        val response = HttpManager.request(
            HttpMethod.POST, getSchema() + GrConstants.AUTH,
            parameters, headers, null
        )
        if (response.status.value == 200) {
            return response.body()
        } else {
            throw HttpException(HttpException.Type.EAUTHFAILED)
        }
    }

    fun setUserWithAccessToken(token: RssToken, response: String) {
        val tokens: Array<String?> = response.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (tokens.size == 3) {
            token.accessToken = tokens[2]!!.substring("Auth=".length)
        } else if (tokens.size == 2) {
            token.accessToken = tokens[1]!!.substring("Auth=".length)
        }
        token.expiresTimestamp = System.currentTimeMillis() + (getExpiredTimestamp() - 300) * 1000 // 提前5分钟过期，避免临界问题
    }

    // ======== oauth2 ========
    fun getOAuth2Url(state: String?): String {
        var state = state
        if (state == null || "" == state) state = "na"
        val scope = "read%20write"
        return getSchema() + String.format(GrConstants.OAUTH2, state, scope)
    }

    suspend fun getRefreshToken(code: String): String? {
        val a = "code=%s&redirect_uri=%s&client_id=%s&client_secret=%s&scope=&grant_type=authorization_code"
        val params = String.format(a, code, Static.REDIRECT_URI, GrConstants.CLIENT_ID, GrConstants.CLIENT_SECRET)

        return HttpManager.request(HttpMethod.POST, getSchema() + GrConstants.TOKEN, null, null, params, false).body()
    }

    suspend fun getAccessTokenOAuth2(refreshToken: String?): String? {
        val a = "client_id=%s&client_secret=%s&grant_type=refresh_token&refresh_token=%s"
        val params = String.format(a, GrConstants.CLIENT_ID, GrConstants.CLIENT_SECRET, refreshToken)

        return HttpManager.request(HttpMethod.POST, getSchema() + GrConstants.TOKEN, null, null, params, false).body()
    }

    fun setUserWithRefreshToken(token: RssToken, response: String) {
        val r = Static.defaultJson.decodeFromString<Oauth2Response>(response)
        token.refreshToken = r.refresh_token
        token.accessToken = r.access_token
        token.expiresTimestamp = (System.currentTimeMillis()
                + (r.expires_in.orZero() - 300) * 1000) // 提前5分钟过期，避免临界问题
    }

    fun setUserWithAccessTokenOAuth2(token: RssToken, response: String) {
        val r = Static.defaultJson.decodeFromString<Oauth2Response>(response)
        token.refreshToken = r.refresh_token
        token.accessToken = r.access_token
        token.expiresTimestamp = (System.currentTimeMillis()
                + (r.expires_in.orZero() - 300) * 1000) // 提前5分钟过期，避免临界问题
    }
}
