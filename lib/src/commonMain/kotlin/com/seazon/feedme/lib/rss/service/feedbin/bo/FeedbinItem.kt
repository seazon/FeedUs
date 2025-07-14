package com.seazon.feedme.lib.rss.service.feedbin.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.utils.DateUtil
import com.seazon.feedme.platform.TimeProvider
import kotlinx.serialization.Serializable

@Serializable
data class FeedbinItem(
    var id: Long = 0,
    var feed_id: Int = 0,
    var title: String? = null,
    var url: String? = null,
    var author: String? = null,
    var content: String? = null,
    var summary: String? = null,
    var created_at: String? = null,
    var published: String? = null,
    var images: Image? = null,
) : Entity() {

    fun convert(): RssItem {
        val item = RssItem()
        item.id = id.toString()
        item.title = title
        val crawled = DateUtil.isoStringToTimestamp(published)
        item.publisheddate = crawled
        item.updateddate = crawled
        item.description = content
        if (item.description == null) {
            item.description = ""
        }
        item.author = if (author == null) "" else author
        item.link = url
        item.feed!!.id = feed_id.toString()
        item.fid = item.feed!!.id
        item.visual = images?.size_1?.cdn_url ?: images?.original_url
        item.isUnread = true
        item.since = created_at
        return item
    }

    @Serializable
    data class Image(
        val original_url: String? = null,
        val size_1: Size? = null,
    ) : Entity() {

        @Serializable
        data class Size(
            val cdn_url: String? = null,
        ) : Entity()
    }
}
