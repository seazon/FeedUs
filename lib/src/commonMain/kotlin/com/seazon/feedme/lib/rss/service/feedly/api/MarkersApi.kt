package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlyUnreadCounts
import com.seazon.feedme.lib.utils.jsonOf

class MarkersApi(feedlyToken: RssToken) : AuthedApi(feedlyToken) {

    suspend fun getUnreadCounts(): FeedlyUnreadCounts? {
        return execute(HttpMethod.GET, FeedlyConstants.URL_MARKERS_COUNTS).convertBody()
    }

    suspend fun markArticleRead(entryIds: Array<String>?): String? {
        val o = jsonOf(
            "action" to "markAsRead",
            "type" to "entries",
            "entryIds" to entryIds?.mapNotNull { it },
        )
        return execute(HttpMethod.POST, FeedlyConstants.URL_MARKERS, null, null, o.toString()).body
    }

    suspend fun keepArticleUnread(entryIds: Array<String>?): String? {
        val o = jsonOf(
            "action" to "keepUnread",
            "type" to "entries",
            "entryIds" to entryIds?.mapNotNull { it },
        )
        return execute(HttpMethod.POST, FeedlyConstants.URL_MARKERS, null, null, o.toString()).body
    }

    suspend fun markFeedRead(feedIds: Array<String?>, asOf: Long): String? {
        val o = jsonOf(
            "action" to "markAsRead",
            "type" to "feeds",
            "feedIds" to feedIds.mapNotNull { it },
            "asOf" to asOf.toString(),
        )
        return execute(HttpMethod.POST, FeedlyConstants.URL_MARKERS, null, null, o.toString()).body
    }

    suspend fun markCategoryRead(categoryIds: Array<String?>, asOf: Long): String? {
        val o = jsonOf(
            "action" to "markAsRead",
            "type" to "categories",
            "categoryIds" to categoryIds.mapNotNull { it },
            "asOf" to asOf.toString(),
        )
        return execute(HttpMethod.POST, FeedlyConstants.URL_MARKERS, null, null, o.toString()).body
    }
}
