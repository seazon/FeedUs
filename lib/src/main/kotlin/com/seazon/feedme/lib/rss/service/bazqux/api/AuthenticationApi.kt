package com.seazon.feedme.lib.rss.service.bazqux.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.gr.GrConfig
import com.seazon.feedme.lib.rss.service.gr.GrConstants
import io.ktor.client.call.body
import java.util.ArrayList
import java.util.HashMap

class AuthenticationApi(token: RssToken, config: GrConfig) :
    com.seazon.feedme.lib.rss.service.gr.api.AuthenticationApi(token, config) {

    override suspend fun getAccessToken(username: String?, password: String?): String? {
        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        parameters.add(NameValuePair("Email", username.orEmpty()))
        parameters.add(NameValuePair("Passwd", password.orEmpty()))

        val response = HttpManager.request(
            HttpMethod.POST, getSchema() + GrConstants.AUTH,
            parameters
        )
        if (response.status.value == 200) {
            return response.body()
        } else {
            throw HttpException(
                HttpException.Type.EAUTHFAILED, errorMap.get(
                    response.headers[HEADER_LOGINERRORREASON]
                )
            )
        }
    }

    companion object {
        private const val HEADER_LOGINERRORREASON = "X-BQ-LoginErrorReason"

        private val errorMap: MutableMap<String?, String?> = HashMap<String?, String?>()

        init {
            errorMap.put("YearSubscriptionExpired", "Login failed: Your subscription has expired")
            errorMap.put("FreeTrialExpired", "Login failed: your free trial has expired")
        }
    }
}
