package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.utils.IntAsStringSerializer
import kotlinx.serialization.Serializable

@Serializable
data class TtrssFeedList(
    val content: List<TtrssFeed>? = null
)

@Serializable
data class TtrssFeed(
    @Serializable(with = IntAsStringSerializer::class)
    val id: String? = null,
    @Serializable(with = IntAsStringSerializer::class)
    val cat_id: String? = null,
    val title: String? = null,
    val last_updated: Long = 0,
    val feed_url: String? = null,
)
