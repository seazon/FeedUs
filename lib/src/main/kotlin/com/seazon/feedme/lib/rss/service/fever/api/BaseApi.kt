package com.seazon.feedme.lib.rss.service.fever.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.fever.FeverConstants
import com.seazon.feedme.lib.rss.service.fever.bo.CommonResponse

open class BaseApi(val token: RssToken) {

    protected fun getSchema(): String {
        return token.host + FeverConstants.API
    }

    suspend fun execute(
        method: String,
        params: MutableList<NameValuePair> = mutableListOf(),
        authCheck: Boolean
    ): SimpleResponse {
        params.add(NameValuePair("api", ""))
        params.add(NameValuePair(method, ""))
        val response = HttpManager.requestWrap(HttpMethod.POST, getSchema(), params, null, null)

        if (response.code != 200) {
            throw HttpException(HttpException.Type.EREMOTE, "HTTP code: ${response.code}")
        }

        if (authCheck) {
            val commonResponse = response.convertBody<CommonResponse>()
            if (commonResponse.auth != 1) {
                throw HttpException(HttpException.Type.EEXPIRED)
            }
        }

        return response
    }

}