package com.seazon.feedme.lib.rss.service.fever.api

import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.rss.bo.RssToken

open class AuthedApi(token: RssToken) : BaseApi(token) {

    suspend fun execute(method: String, params: MutableList<NameValuePair> = mutableListOf()): SimpleResponse {
        params.add(NameValuePair("api_key", token.auth.orEmpty()))
        return super.execute(method, params, true)
    }

}