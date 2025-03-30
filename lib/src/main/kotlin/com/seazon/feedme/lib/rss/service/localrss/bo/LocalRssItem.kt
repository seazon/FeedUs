package com.seazon.feedme.lib.rss.service.localrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.service.Static
import kotlinx.serialization.Serializable

@Serializable
data class LocalRssItem(
    var id: String? = null,
    var fid: String? = null,
    var author: String? = null,
    var content: String? = null,
    var crawlTime: Long = 0,
    var publishTime: Long = 0,
    var thumbnail: String? = null,
    var title: String? = null,
    var url: String? = null,
) : Entity() {

    fun convert(): RssItem {
        val item = RssItem()
        item.id = id.toString()
        item.title = title
        item.publisheddate = publishTime
        item.updateddate = publishTime
        item.description = content
        item.author = if (author == null) "" else author
        item.link = url
        item.visual = thumbnail
        item.feed!!.id = fid.toString()
        item.fid = item.feed!!.id
        item.isUnread = false
        item.since = publishTime.toString()
        return item
    }

    companion object {
        fun parse(json: String?): LocalRssItem? {
            return Static.defaultJson.decodeFromString<LocalRssItem?>(json.orEmpty())
        }
    }
}