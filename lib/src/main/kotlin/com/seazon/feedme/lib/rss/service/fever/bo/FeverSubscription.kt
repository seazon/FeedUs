package com.seazon.feedme.lib.rss.service.fever.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssTag
import kotlinx.serialization.Serializable

@Serializable
data class FeverSubscription(
    var id: Int,
    var favicon_id: Int,
    var title: String,
    var url: String,
    var site_url: String,
    var is_spark: Int,
    var last_updated_on_time: Long,
    var categories: MutableList<Group>?
) : Entity() {

    fun convert(): RssFeed {
        val feed = RssFeed()
        feed.id = id.toString()
        feed.title = title
        feed.url = site_url
        feed.feedUrl = url
        feed.favicon = null
        feed.categories = categories?.map {
            RssTag(it.id.toString(), it.title)
        } ?: ArrayList()

        return feed
    }

}