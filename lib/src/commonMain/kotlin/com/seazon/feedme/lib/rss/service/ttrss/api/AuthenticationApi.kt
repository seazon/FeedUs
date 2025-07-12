package com.seazon.feedme.lib.rss.service.ttrss.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.ttrss.TtrssConstants
import com.seazon.feedme.lib.rss.service.ttrss.bo.AuthResponse
import com.seazon.feedme.lib.utils.StringUtil
import com.seazon.feedme.lib.utils.base64
import com.seazon.feedme.lib.utils.toJson
import com.seazon.feedme.platform.TimeProvider

class AuthenticationApi(token: RssToken) : BaseApi(token) {
    suspend fun getAccessToken(username: String, password: String, httpUsername: String?, httpPassword: String?): String {
        val o = mapOf(
            "user" to username,
            "password" to password,
        )
        val u = if (httpUsername.isNullOrEmpty()) username else httpUsername
        val p = if (httpPassword.isNullOrEmpty()) password else httpPassword
        val token = StringUtil.format(
            TtrssConstants.HTTP_HEADER_AUTHORIZATION_VALUE,
            "${u}:${p}".base64()
        )
        val headers = mapOf(
            TtrssConstants.HTTP_HEADER_AUTHORIZATION_KEY to token,
        )
        val response = execute(TtrssConstants.METHOD_LOGIN, o, headers)
        if (response.code == 200) {
            return response.body
        } else {
            throw HttpException(HttpException.Type.EAUTHFAILED)
        }
    }

    fun setUserWithAccessToken(token: RssToken, response: String) {
        val o = toJson<AuthResponse>(response)
        val content = o.content
        if (!content?.error.isNullOrEmpty()) {
            throw HttpException(HttpException.Type.EREMOTE, content.error)
        }
        token.auth = content?.sessionId
        val username = if (token.httpUsername.isNullOrEmpty()) token.username else token.httpUsername
        val password = if (token.httpPassword.isNullOrEmpty()) token.password else token.httpPassword
        val t = StringUtil.format(
            TtrssConstants.HTTP_HEADER_AUTHORIZATION_VALUE,
            "${username}:${password}".base64()
        )
        token.accessToken = t
        token.expiresTimestamp = TimeProvider.currentTimeMillis() + getExpiredTimestamp() * 1000
    }
}
