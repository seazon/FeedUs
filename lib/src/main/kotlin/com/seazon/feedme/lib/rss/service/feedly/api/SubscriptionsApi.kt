package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlySubscription
import com.seazon.feedme.lib.utils.jsonOf
import io.ktor.client.call.body
import java.net.URLEncoder

class SubscriptionsApi(feedlyToken: RssToken) : AuthedApi(feedlyToken) {

    suspend fun getSubscriptions(): List<FeedlySubscription>? {
        return execute(HttpMethod.GET, FeedlyConstants.URL_SUBSCRIPTIONS).body()
    }

    suspend fun subscribeFeed(title: String, feedId: String, categories: Array<String>?): String {
        val array = categories?.map {
            jsonOf(
                "id" to "user/" + token.id + "/category/" + it,
                "label" to if (FeedlyConstants.isIgnoredTag(it)) "" else it,
            )
        }
        val o = jsonOf(
            "id" to feedId,
            "title" to title,
            "sortid" to "",
            "categories" to array,
        )
        return execute(HttpMethod.POST, FeedlyConstants.URL_SUBSCRIPTIONS, null, null, o.toString()).body()
    }

    suspend fun unsubscribeFeed(feedId: String): String {
        return execute(
            HttpMethod.DELETE,
            FeedlyConstants.URL_SUBSCRIPTIONS + "/" + URLEncoder.encode(feedId, HttpUtils.DEFAULT_CHARSET)
        ).body()
    }

    suspend fun updateSubscription(feedId: String, title: String, aCategories: Array<String>?): String {
        val request = jsonOf(
            "id" to feedId,
            "title" to title,
            "categories" to aCategories?.map {
                jsonOf(
                    "id" to "user/" + token.id + "/category/" + it,
                    "label" to it
                )
            },
        )
        return execute(
            HttpMethod.POST,
            FeedlyConstants.URL_SUBSCRIPTIONS,
            null, null, request.toString()
        ).body()
    }
}
