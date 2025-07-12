package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssEnclosure
import com.seazon.feedme.lib.rss.bo.RssItem
import kotlinx.serialization.Serializable

@Serializable
class FeedlyItem : Entity() {
    /**
     * Sample :
     * wPHsXItwmGKiIMdSnDrt8IVyU7Dt1StOsAVjW8L+cXc=_14785b533e9:239b0a:28f8b969
     */
    var id: String? = null
    var keywords: List<String>? = null
    var fingerprint: String? = null
    var originId: String? = null
    var title: String? = null
    var author: String? = null
    var actionTimestamp: Long = 0
    var crawled: Long = 0
    var alternate: List<FeedlyAlternate>? = null
    var canonical: List<FeedlyAlternate>? = null
    var tags: List<FeedlyTag>? = null
    var summary: FeedlySummary? = null
    var content: FeedlySummary? = null
    var origin: FeedlyOrigin? = null
    var enclosure: List<FeedlyEnclosure>? = null
    var unread = false
    var visual: FeedlyVisual? = null

    fun convert(): RssItem {
        val rssItem = RssItem()
        rssItem.id = id
        rssItem.title = title
        rssItem.publisheddate = if (crawled == 0L) null else crawled
        rssItem.updateddate = actionTimestamp

        if (tags != null) {
            for (tag in tags!!) {
                rssItem.addTag(tag.label)
            }
        }
        rssItem.description = content?.content ?: summary?.content.orEmpty()
        var audioUrl: String? = null
        var audioSize = 0
        rssItem.enclosure = enclosure?.map {
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
        // 考虑到和feedly web版的一致性，缩略图也要出现在正文，如果正文中没有这张图的话
        if (visual != null) { // visual和正文url重复
            if (rssItem.description?.contains(visual!!.url.orEmpty()) == false) {
                val newList = rssItem.enclosure.orEmpty().toMutableList().apply {
                    add(
                        0, RssEnclosure(
                            href = visual!!.url,
                            type = "image/jpg",
                        )
                    )
                }
                rssItem.enclosure = newList
            }
        }
        rssItem.podcastUrl = audioUrl
        rssItem.podcastSize = audioSize
        rssItem.author = if (author == null) "" else author
        rssItem.link = if (alternate.isNullOrEmpty()) "" else alternate!!.first().href
        rssItem.feed?.id = origin!!.streamId
        rssItem.feed?.title = origin!!.title
        rssItem.feed?.url = origin!!.htmlUrl
        // TODO: other service also need consider this problem: how to solve generate feed from item but feed url is empty
        rssItem.feed?.feedUrl = if (origin!!.streamId == null) null else origin!!.streamId?.substring(5)
        rssItem.fid = rssItem.feed?.id
        rssItem.visual = if (visual == null) null else visual!!.url
        rssItem.isUnread = unread
        rssItem.since = crawled.toString()
        return rssItem
    }
}

fun Collection<FeedlyItem>.convert(): List<RssItem> = map {
    it.convert()
}