package com.seazon.feedme.lib.rss.service.folo.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import io.ktor.client.statement.HttpResponse

open class AuthedApi(val token: RssToken) : BaseApi() {

    private fun setHeaderToken(headers: MutableMap<String, String>) {
        headers["Cookie"] = "__Secure-better-auth.session_token=${token.accessToken.orEmpty()}"
        headers[HttpUtils.HTTP_HEADERS_CONTENT_TYPE] = HttpUtils.HTTP_HEADERS_CONTENT_TYPE_JSON
    }

    protected suspend fun execute(
        httpMethod: HttpMethod,
        url: String,
        params: List<NameValuePair>? = null,
        headers: MutableMap<String, String>? = null,
        body: String? = null,
    ): HttpResponse {
        var headers = headers
        if (headers == null) headers = HashMap()
        setHeaderToken(headers)
        val response = HttpManager.request(httpMethod, getSchema() + url, params, headers, body)
        if (response.status.value == RssApi.HTTP_CODE_UNAUTHORIZED) {
            throw HttpException(HttpException.Type.EEXPIRED)
        } else if (response.status.value == RssApi.HTTP_CODE_BAD_REQUEST) {
            throw HttpException(HttpException.Type.EREMOTE, "HTTP code: " + response.status.value)
        }
        return response
    }
}
