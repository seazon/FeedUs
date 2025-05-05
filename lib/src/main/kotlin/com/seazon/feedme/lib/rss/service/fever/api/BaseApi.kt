package com.seazon.feedme.lib.rss.service.fever.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.fever.FeverConstants
import com.seazon.feedme.lib.rss.service.fever.bo.CommonResponse
import com.seazon.feedme.lib.utils.toJson
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

open class BaseApi(val token: RssToken) {

    protected fun getSchema(): String {
        return token.host + FeverConstants.API
    }

    suspend fun execute(
        method: String,
        params: MutableList<NameValuePair> = mutableListOf(),
        authCheck: Boolean
    ): HttpResponse {
        params.add(NameValuePair("api", ""))
        params.add(NameValuePair(method, ""))
        val response = HttpManager.request(HttpMethod.POST, getSchema(), params, null, null)

        if (response.status.value != 200) {
            throw HttpException(HttpException.Type.EREMOTE, "HTTP code: ${response.status.value}")
        }

        if (authCheck) {
            val commonResponse = toJson<CommonResponse>(response.body())
            if (commonResponse.auth != 1) {
                throw HttpException(HttpException.Type.EEXPIRED)
            }
        }

        return response
    }

}