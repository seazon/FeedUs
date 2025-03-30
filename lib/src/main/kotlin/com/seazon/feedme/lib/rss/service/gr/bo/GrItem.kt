package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssEnclosure
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.service.gr.GrApi
import com.seazon.feedme.lib.rss.service.gr.GrConstants
import com.seazon.feedme.lib.utils.HtmlUtils
import com.seazon.feedme.lib.utils.RssUtils
import kotlinx.serialization.Serializable

@Serializable
data class GrItem(
    /**
     * Sample : tag:google.com,2005:reader/item/00000000148b9369
     */
    var id: String? = null,
    var title: String? = null,
    var author: String? = null,
    var published: Long = 0,
    var updated: Long = 0,
    var starred: Long = 0,
    var crawlTimeMsec: Long = 0,
    var alternate: List<GrAlternate>? = null,
    var canonical: List<GrAlternate>? = null,
    var categories: List<String>? = null,
    var summary: GrSummary? = null,
    var content: GrSummary? = null,
    var origin: GrOrigin? = null,
    var enclosure: List<GrEnclosure>? = null,
) : Entity() {

    val isUnread: Boolean
        get() {
            for (tag in categories!!) {
                if (tag.endsWith(GrConstants.GLOBAL_STATE_READ)) {
                    return false
                }
            }
            return true
        }

    fun convert(): RssItem {
        val item = RssItem()
        item.id = id
        item.title = title
        val publishTimeMsec = RssUtils.parsePublishTime(crawlTimeMsec, published)
        item.publisheddate = if (publishTimeMsec == 0L) null else publishTimeMsec
        item.updateddate = if (updated == 0L) null else updated
        if (categories != null) {
            for (tag in categories!!) {
                if (GrApi.isLabel(tag)) {
                    item.addTag(GrApi.getLabel(tag))
                }
            }
        }

        item.description = if (content == null) null else (content!!.content)
        if (item.description == null || item.description == "") {
            item.description = if (summary == null) null else (summary!!.content)
        }
        var audioUrl: String? = null
        var audioSize = 0
        item.enclosure = enclosure?.map {
            if (audioUrl == null && it.type != null && (it.type!!.startsWith("audio/") || it.type!!.startsWith("text/"))) {
                audioUrl = it.href
                audioSize = it.length.toInt()
            }
            RssEnclosure(
                href = it.href,
                type = it.type,
                length = it.length
            )
        }
        if (item.description == null) {
            item.description = ""
        }
        item.podcastUrl = audioUrl
        item.podcastSize = audioSize

        item.author = if (author == null) "" else author
        item.link = if (alternate == null || alternate!!.size == 0) "" else alternate!![0].href

        if (origin != null) {
            item.feed!!.id = origin!!.streamId
            item.feed!!.url = origin!!.htmlUrl
            item.feed!!.title = origin!!.title
        }
        item.fid = item.feed!!.id

        item.visual = HtmlUtils.getFirstImage(item.description, item.link)
        item.isUnread = isUnread
        item.since = published.toString()
        return item
    }
}
