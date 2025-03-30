package com.seazon.feedme.lib.rss.service.fever.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.rss.service.fever.FeverConstants
import com.seazon.feedme.lib.rss.service.fever.bo.CommonResponse
import com.seazon.feedme.lib.utils.Helper
import io.ktor.client.call.body

class AuthenticationApi(token: RssToken) : BaseApi(token) {

    suspend fun getAccessToken(username: String?, password: String?): String {
        val response = execute(FeverConstants.METHOD_LOGIN, mutableListOf<NameValuePair>().apply {
            add(NameValuePair("email", username.orEmpty()))
            add(NameValuePair("pass", password.orEmpty()))
            add(NameValuePair("api_key", Helper.md52("$username:$password").toLowerCase()))
        }, false)

        val rr = response.body<String>()
        val commonResponse = Static.defaultJson.decodeFromString<CommonResponse>(rr)
        if (commonResponse.auth != 1) {
            throw HttpException(HttpException.Type.EAUTHFAILED)
        }

        return rr
    }

    fun setUserWithAccessToken(token: RssToken, response: String) {
        val o = Static.defaultJson.decodeFromString<CommonResponse>(response)
        val content = o.auth
        if (content != 1) {
            throw HttpException(HttpException.Type.EREMOTE, "Auth failed")
        }
        val t = Helper.md52(token.username + ":" + token.password).toLowerCase()
        token.auth = t
        token.accessToken = t
        token.expiresTimestamp = System.currentTimeMillis() + 3600 * 1000
    }
}