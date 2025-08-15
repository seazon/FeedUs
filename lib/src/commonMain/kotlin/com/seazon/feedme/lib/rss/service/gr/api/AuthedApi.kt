package com.seazon.feedme.lib.rss.service.gr.api

import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.SimpleResponse
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrConfig
import com.seazon.feedme.lib.rss.service.gr.GrConstants
import com.seazon.feedme.lib.utils.format

open class AuthedApi(token: RssToken, config: GrConfig, val api: RssApi) : BaseApi(token, config) {

    protected fun setHeaderToken(headers: MutableMap<String, String>) {
        if (api.getAuthType() == RssApi.AUTH_TYPE_OAUTH2) {
            headers[GrConstants.HTTP_HEADER_AUTHORIZATION_KEY] =
                GrConstants.HTTP_HEADER_AUTHORIZATION_VALUE_OAUTH2.format(token.accessToken)
        } else if (api.getAuthType() == RssApi.AUTH_TYPE_BASE) {
            headers[GrConstants.HTTP_HEADER_AUTHORIZATION_KEY] =
                GrConstants.HTTP_HEADER_AUTHORIZATION_VALUE.format(token.accessToken)
        } else {
        }
    }

    protected suspend fun execute(
        httpMethod: HttpMethod,
        url: String,
        params: List<NameValuePair>? = null,
        headers: MutableMap<String, String>? = null,
        body: String? = null,
        json: Boolean = true,
    ): SimpleResponse {
        var headers = headers
        if (headers == null) {
            headers = HashMap<String, String>()
        }
        setHeaderToken(headers)
        if (isTheseRssType(Static.ACCOUNT_TYPE_INOREADER_OAUTH2, Static.ACCOUNT_TYPE_INOREADER)) {
            setHeaderAppAuthentication(headers)
        }
        val response = HttpManager.requestWrap(
            httpMethod = httpMethod,
            url = getSchema() + url,
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
}
