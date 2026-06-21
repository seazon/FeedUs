package com.seazon.feedme.lib.rss.service.ttrss.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils.HTTP_HEADERS_CONTENT_TYPE
import com.seazon.feedme.lib.network.HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.ttrss.TtrssConstants
import com.seazon.feedme.lib.utils.toJsonElement

open class BaseApi(val token: RssToken) {

    protected fun getSchema(): String {
        return token.host + TtrssConstants.API
    }

    protected fun getExpiredTimestamp(): Long {
        return TtrssConstants.EXPIRED_TIMESTAMP
    }

    suspend fun execute(method: String, bodyMap: Map<String, Any>?, headers: Map<String, String>?): SimpleResponse {
        val bodyMutableMap = bodyMap?.toMutableMap() ?: mutableMapOf()
        bodyMutableMap["op"] = method

        val response: SimpleResponse = HttpManager.requestWrap(
            httpMethod = HttpMethod.POST,
            url = getSchema(),
            params = null,
            headers = headers.orEmpty() + (HTTP_HEADERS_CONTENT_TYPE to HTTP_HEADERS_CONTENT_TYPE_JSON),
            body = bodyMutableMap.toJsonElement().toString()
        )
        if (response.body.contains("NOT_LOGGED_IN")) {
            throw HttpException(HttpException.Type.EEXPIRED)
        }
        return response
    }
}
