package com.seazon.feedme.lib.rss.service.folo.bo

import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.utils.DateUtil
import kotlinx.serialization.Serializable

@Serializable
data class FoloEntries(
    val read: Boolean? = null,
    val view: Int? = null,
    val entries: FoloEntry? = null,
    val feeds: FoloFeed? = null,
) {
    fun convert(): RssItem? {
        val entry = entries
        val feed = feeds
        return entry?.convert(feed, read)
    }
}

@Serializable
data class FoloEntry(
    val title: String? = null,
    val url: String? = null,
    val content: String? = null,
    val description: String? = null,
    val id: String? = null,
    val author: String? = null,
    val publishedAt: String? = null,
) {
    fun convert(feed: FoloFeed?, read: Boolean?): RssItem {
        return RssItem(
            id = id,
            fid = feed?.id,
            title = title,
            link = url,
            author = author,
            publisheddate = DateUtil.isoStringToTimestamp(publishedAt),
            updateddate = DateUtil.isoStringToTimestamp(publishedAt),
            description = content,
            tags = null,
            visual = null,
            isUnread = !(read ?: false),
        )
    }
}
