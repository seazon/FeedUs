package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import kotlinx.serialization.Serializable

@Serializable
data class FeedlyUnreadCounts(
    var max: Int = 0,
    var unreadcounts: List<FeedlyUnreadCount> = emptyList(),
) {

    fun convert(): RssUnreadCounts {
        return RssUnreadCounts(max, unreadcounts.convert())
    }

}