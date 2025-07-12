package com.seazon.feedme.lib.rss.service.feedbin.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedbin.FeedbinConstants
import com.seazon.feedme.lib.utils.Helper
import com.seazon.feedme.lib.utils.format
import com.seazon.feedme.platform.TimeProvider

class AuthenticationApi(token: RssToken) : BaseApi(token) {

    suspend fun getAccessToken(email: String?, password: String?): String {
        val token = FeedbinConstants.HTTP_HEADER_AUTHORIZATION_VALUE.format(Helper.base64("$email:$password"))
        val headers: MutableMap<String, String> = HashMap<String, String>()
        headers.put(FeedbinConstants.HTTP_HEADER_AUTHORIZATION_KEY, token)
        val response = HttpManager.requestWrap(HttpMethod.GET, getSchema() + FeedbinConstants.AUTH, null, headers, null)

        if (response.code == 200) {
            return token
        } else {
            throw HttpException(HttpException.Type.EAUTHFAILED)
        }
    }

    fun setUserWithAccessToken(token: RssToken, response: String?) {
        token.auth = response
        token.expiresTimestamp = TimeProvider.currentTimeMillis() + getExpiredTimestamp() * 1000
    }
}

