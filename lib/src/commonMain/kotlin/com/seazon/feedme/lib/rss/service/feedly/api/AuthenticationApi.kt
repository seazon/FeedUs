package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.Oauth2Response
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.utils.format
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedme.lib.utils.toJson
import com.seazon.feedme.platform.TimeProvider
import io.ktor.client.call.body

class AuthenticationApi : BaseApi() {

    fun getOAuth2Url(state: String?): String {
        var state = state
        if (state == null || "" == state) state = "na"
        return FeedlyConstants.SCHEMA_HTTPS + FeedlyConstants.AUTH.format(state)
    }

    suspend fun getRefreshToken(code: String?): String {
        val parameters = listOf(
            NameValuePair("code", code.orEmpty()),
            NameValuePair("client_id", FeedlyConstants.CLIENT_ID),
            NameValuePair("client_secret", FeedlyConstants.CLIENT_SECRET),
            NameValuePair("redirect_uri", Static.REDIRECT_URI_OLD),
            NameValuePair("state", "na"),
            NameValuePair("grant_type", "authorization_code"),
        )
        return HttpManager.request(HttpMethod.POST, getSchema() + FeedlyConstants.TOKEN, parameters).body()
    }

    suspend fun getAccessToken(refreshToken: String?): String {
        val parameters = listOf(
            NameValuePair("client_id", FeedlyConstants.CLIENT_ID),
            NameValuePair("client_secret", FeedlyConstants.CLIENT_SECRET),
            NameValuePair("refresh_token", refreshToken.orEmpty()),
            NameValuePair("grant_type", "refresh_token"),
        )
        return HttpManager.request(HttpMethod.POST, getSchema() + FeedlyConstants.TOKEN, parameters).body()
    }

    fun setUserWithAccessToken(token: RssToken, response: String) {
        val r = toJson<Oauth2Response>(response)
        token.id = r.id
        token.accessToken = r.access_token
        token.expiresTimestamp = (TimeProvider.currentTimeMillis()
                + (r.expires_in.orZero() - 300) * 1000) // 提前5分钟过期，避免临界问题
    }

    fun setUserWithRefreshToken(token: RssToken, response: String) {
        val r = toJson<Oauth2Response>(response)
        token.id = r.id
        token.refreshToken = r.refresh_token
        token.accessToken = r.access_token
        token.expiresTimestamp = (TimeProvider.currentTimeMillis()
                + (r.expires_in.orZero() - 300) * 1000) // 提前5分钟过期，避免临界问题
    }
}
