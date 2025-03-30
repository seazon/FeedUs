package com.seazon.feedme.lib.rss.service.feedbin.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedbin.FeedbinConstants
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinSubscription
import com.seazon.feedme.lib.utils.jsonOf
import io.ktor.client.call.body

class SubscriptionsApi(token: RssToken) : AuthedApi(token) {

    suspend fun getSubscriptions(): List<FeedbinSubscription>? {
        return execute(HttpMethod.GET, FeedbinConstants.URL_SUBSCRIPTIONS).body()
    }

    suspend fun createSubscriptions(url: String?): String? {
        val o = jsonOf(
            "feed_url" to url
        )
        return execute(HttpMethod.POST, FeedbinConstants.URL_SUBSCRIPTIONS, null, null, o.toString()).body()
    }

    suspend fun deleteSubscriptions(id: String): String? {
        return execute(HttpMethod.DELETE, String.format(FeedbinConstants.URL_SUBSCRIPTIONS_DELETE, id)).body()
    }
}
