package com.seazon.feedme.lib.rss.service.ttrss.api

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.ttrss.TtrssConstants
import com.seazon.feedme.lib.utils.toJsonElement
import io.ktor.client.statement.HttpResponse

//import com.seazon.feedme.core.Core
//import com.seazon.feedme.ext.api.lib.http.HttpException
//import com.seazon.feedme.ext.api.lib.http.HttpMethod
//import com.seazon.feedme.ext.api.lib.http.HttpResponse
//import com.seazon.feedme.rss.ttrss.TtrssConstants
//import org.json.JSONException
//import org.json.JSONObject

open class BaseApi(val token: RssToken) {
//    protected var core: Core
//
//    init {
//        this.core = core
//    }

    protected fun getSchema(): String {
        return token.host + TtrssConstants.API
    }

    protected fun getExpiredTimestamp(): Long {
        return TtrssConstants.EXPIRED_TIMESTAMP
    }

    suspend fun execute(method: String, bodyMap: Map<String, Any>?, headers: Map<String, String>?): HttpResponse {
        val bodyMutableMap = bodyMap?.toMutableMap() ?: mutableMapOf<String, Any>()
        bodyMutableMap.put("op", method)

        val response: HttpResponse = HttpManager.request(HttpMethod.POST, getSchema(), null, headers, bodyMutableMap.toJsonElement().toString())
//        if (response.getBody().toString().contains("NOT_LOGGED_IN")) {
//            throw HttpException(HttpException.Type.EEXPIRED)
//        } // TODO can't parse response in basic method, need to refactor
        return response
    }
}
