package com.seazon.feedme.lib.rss.service.fever.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.fever.FeverConstants
import com.seazon.feedme.lib.rss.service.fever.bo.CommonResponse
import com.seazon.feedme.lib.utils.Helper
import com.seazon.feedme.lib.utils.toJson
import com.seazon.feedme.platform.TimeProvider

class AuthenticationApi(token: RssToken) : BaseApi(token) {

    suspend fun getAccessToken(username: String?, password: String?): String {
        val response = execute(
            method = FeverConstants.METHOD_LOGIN,
            params = mutableListOf<NameValuePair>().apply {
                add(NameValuePair("email", username.orEmpty()))
                add(NameValuePair("pass", password.orEmpty()))
                add(NameValuePair("api_key", Helper.md52("$username:$password").lowercase()))
            },
            authCheck = false
        )

        val commonResponse = response.convertBody<CommonResponse>()
        if (commonResponse.auth != 1) {
            throw HttpException(HttpException.Type.EAUTHFAILED)
        }

        return response.body
    }

    fun setUserWithAccessToken(token: RssToken, response: String) {
        val o = toJson<CommonResponse>(response)
        val content = o.auth
        if (content != 1) {
            throw HttpException(HttpException.Type.EREMOTE, "Auth failed")
        }
        val t = Helper.md52(token.username + ":" + token.password).lowercase()
        token.auth = t
        token.accessToken = t
        token.expiresTimestamp = TimeProvider.currentTimeMillis() + 3600 * 1000
    }
}