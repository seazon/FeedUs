package com.seazon.feedme.lib.readlater.karakeep

import com.seazon.feedme.lib.LocalConstants
import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.readlater.ReadLaterApi
import com.seazon.feedme.lib.readlater.bo.Tag
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.utils.jsonOf

class KarakeepApi(val apiBaseUrl: String, val apiKey: String) : ReadLaterApi {

    override suspend fun add(url: String, title: String?): String? {
        val o = jsonOf(
            "type" to "link",
            "url" to url,
            "title" to title?.take(1000),
        )
        return execute(HttpMethod.POST, "/bookmarks", null, null, o.toString()).body
    }

    override suspend fun delete(id: String): String? {
        return execute(HttpMethod.DELETE, "/bookmarks/$id", null, null, null).body
    }

    override suspend fun attachTags(id: String, tags: List<String>): String? {
        val o = jsonOf(
            "tags" to tags.map {
                Tag(tagName = it)
            }
        )
        return execute(HttpMethod.POST, "/bookmarks/$id/tags", null, null, o.toString()).body
    }

    override suspend fun detachTags(id: String, tags: List<String>): String? {
        val o = jsonOf(
            "tags" to tags.map {
                Tag(tagName = it)
            }
        )
        return execute(HttpMethod.DELETE, "/bookmarks/$id/tags", null, null, o.toString()).body
    }

    private suspend fun execute(
        httpMethod: HttpMethod,
        url: String,
        params: MutableList<NameValuePair>? = null,
        headers: MutableMap<String, String>? = null,
        body: String? = null,
        json: Boolean = true
    ): SimpleResponse {
        var headers = headers
        if (headers == null) {
            headers = HashMap()
        }
        setHeaderToken(headers)
        val response = HttpManager.requestWrap(
            httpMethod = httpMethod,
            url = apiBaseUrl + url,
            params = params,
            headers = headers,
            body = body,
            json = json
        )
        if (response.code == RssApi.HTTP_CODE_UNAUTHORIZED) {
            throw HttpException(HttpException.Type.EEXPIRED)
        }
        return response
    }

    private fun setHeaderToken(headers: MutableMap<String, String>) {
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        headers["Authorization"] = "Bearer $apiKey"
    }
}