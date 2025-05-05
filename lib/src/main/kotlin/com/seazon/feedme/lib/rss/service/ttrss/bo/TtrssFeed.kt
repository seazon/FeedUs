package com.seazon.feedme.lib.rss.service.ttrss.bo

import kotlinx.serialization.Serializable

@Serializable
data class TtrssFeedList(
    val content: List<TtrssFeed>? = null
)

@Serializable
data class TtrssFeed(
    val id: Int? = null,
    val cat_id: Int? = null,
    val title: String? = null,
    val last_updated: Long = 0,
    val feed_url: String? = null,
)
