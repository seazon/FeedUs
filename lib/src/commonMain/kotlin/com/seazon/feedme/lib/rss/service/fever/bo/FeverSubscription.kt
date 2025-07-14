package com.seazon.feedme.lib.rss.service.fever.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssTag
import kotlinx.serialization.Serializable

@Serializable
data class FeverSubscription(
    var id: Int? = null,
    var favicon_id: Int? = null,
    var title: String? = null,
    var url: String? = null,
    var site_url: String? = null,
    var is_spark: Int? = null,
    var last_updated_on_time: Long? = null,
    var categories: MutableList<Group>? = null,
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