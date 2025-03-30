package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.RssUnreadCount
import kotlinx.serialization.Serializable

/**
 * Sample : feed/http://daichuanqing.com/index.php/feed
 * Sample : user/c805fcbf-3acf-4302-a97e-d82f9d7c897f/category/global.all
 * Sample : user/c805fcbf-3acf-4302-a97e-d82f9d7c897f/category/design
 */
@Serializable
data class FeedlyUnreadCount(
    var id: String? = null,
    var count: Int = 0,
    var updated: Long = 0,
) {
    fun convert(): RssUnreadCount {
        return RssUnreadCount(id, count, updated)
    }
}

fun Collection<FeedlyUnreadCount>.convert(): List<RssUnreadCount> = map {
    it.convert()
}