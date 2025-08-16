package com.seazon.feedme.lib.rss.service.fever.api

import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.rss.bo.RssToken

open class AuthedApi(token: RssToken) : BaseApi(token) {

    suspend fun execute(method: String, xFormParams: MutableList<NameValuePair> = mutableListOf()): SimpleResponse {
        val params = mutableListOf<NameValuePair>()
        xFormParams.add(NameValuePair("api_key", token.auth.orEmpty()))
        return super.execute(method = method, params = params, xFormParams = xFormParams, authCheck = true)
    }

}