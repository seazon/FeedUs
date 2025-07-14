package com.seazon.feedme.lib.rss.service.fever.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.utils.HtmlUtils
import com.seazon.feedme.lib.utils.RssUtils
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable

@Serializable
data class FeverStream(
    var items: MutableList<FeverItem> = mutableListOf(),
    var ids: MutableList<String> = mutableListOf(),
    var unread_item_ids: String = "",
    var saved_item_ids: String = ""
) : Entity() {

    companion object {

        fun parse(json: String?): RssStream {
            if (json.isNullOrBlank()) {
                return RssStream()
            }
            val feverStream = toJson<FeverStream>(json).apply {
                if (unread_item_ids.isNotBlank()) {
                    ids = unread_item_ids.split(",").toMutableList()
                }
                if (saved_item_ids.isNotBlank()) {
                    ids = saved_item_ids.split(",").toMutableList()
                }
            }
            return RssStream(null, feverStream.items.map { it.convert() }, feverStream.ids)
        }
    }
}

@Serializable
data class FeverItem(
    var id: Long? = null,
    var feed_id: Long? = null,
    var title: String? = null,
    var author: String? = null,
    var html: String? = null,
    var url: String? = null,
    var is_saved: Int? = null,
    var is_read: Int? = null,
    var created_on_time: Long? = null,
) : Entity() {

    fun convert(): RssItem {
        val item = RssItem()
        item.id = id.toString()
        item.title = title
        // User feedback created_on_time is second, not millisecond. So *1000 if length == 10
        val crawled = RssUtils.parsePublishTime(created_on_time, null)
        item.publisheddate = if (crawled == 0L) null else crawled
        item.updateddate = crawled
        item.description = html.orEmpty()
        item.author = if (author == null) "" else author
        item.link = url
        item.feed?.id = feed_id.toString()
        item.fid = item.feed?.id
        item.visual = HtmlUtils.getFirstImage(item.description, item.link)
        item.isUnread = is_read == 0
        item.since = crawled.toString()
        return item
    }

}