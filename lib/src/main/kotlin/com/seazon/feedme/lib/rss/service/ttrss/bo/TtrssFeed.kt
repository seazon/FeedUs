package com.seazon.feedme.lib.rss.service.ttrss.bo

data class TtrssFeedList(
    val content: List<TtrssFeed>? = null
)

data class TtrssFeed(
    val id: String? = null,
    val cat_id: String? = null,
    val title: String? = null,
    val last_updated: Long = 0,
    val feed_url: String? = null,
)
