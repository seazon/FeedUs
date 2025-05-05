package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssEnclosure
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.service.ttrss.TtrssApi
import com.seazon.feedme.lib.utils.HtmlUtils
import kotlinx.serialization.Serializable

@Serializable
data class TtrssItem(
    var id: Int? = null,
    var title: String? = null,
    var link: String? = null,
    var unread: Boolean = false,
    var author: String? = null,
    var updated: Long = 0,
    var feed_id: Int? = null,
    var feed_title: String? = null,
    var content: String? = null,
    var attachments: MutableList<TtrssAttachments>? = null,
) : Entity() {

    fun convert(): RssItem {
        val item = RssItem()
        item.id = id.toString()
        item.title = title
        item.publisheddate = if (updated == 0L) null else updated * 1000
        item.updateddate = item.publisheddate
        item.description = content
        if (item.description == null) {
            item.description = ""
        }
        item.author = if (author == null) "" else author
        item.link = link
        item.feed?.let {
            it.id = TtrssApi.wrapFeedId(feed_id.toString())
            it.title = feed_title
        }
        item.fid = item.feed?.id
        item.visual = HtmlUtils.getFirstImage(item.description, item.link)
        var audioUrl: String? = null
        item.enclosure = attachments?.map {
            if (audioUrl == null && it.content_type?.startsWith("audio/") == true) {
                audioUrl = it.content_url
            }
            RssEnclosure(
                href = it.content_url,
                type = it.content_type,
            )
        }
        item.podcastUrl = audioUrl
        item.podcastSize = 0
        item.isUnread = unread
        item.since = id.toString()
        return item
    }
}
