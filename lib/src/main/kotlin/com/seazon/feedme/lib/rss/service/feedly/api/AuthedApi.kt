package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import io.ktor.client.statement.HttpResponse

open class AuthedApi(val token: RssToken) : BaseApi() {

    private fun setHeaderToken(headers: MutableMap<String, String>) {
        headers[FeedlyConstants.HTTP_HEADER_AUTHORIZATION_KEY] =
            String.format(FeedlyConstants.HTTP_HEADER_AUTHORIZATION_VALUE, token.accessToken)
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
