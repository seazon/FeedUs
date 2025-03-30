package com.seazon.feedme.lib.rss.service.ttrss.api

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.ttrss.TtrssConstants
import io.ktor.client.statement.HttpResponse
import java.util.HashMap

open class AuthedApi(token: RssToken) : BaseApi(token) {

    suspend fun execute(method: String, bodyMap: Map<String, Any>?): HttpResponse {
        val bodyMutableMap = bodyMap?.toMutableMap() ?: mutableMapOf<String, Any>()
        bodyMutableMap.put("sid", token.auth.orEmpty())

        var headers: MutableMap<String, String>? = null
        if (!token.accessToken.isNullOrEmpty()) {
            headers = HashMap<String, String>()
            headers.put(TtrssConstants.HTTP_HEADER_AUTHORIZATION_KEY, token.accessToken.orEmpty())
        }

        return super.execute(method, bodyMutableMap, headers)
    }
}
