package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class FoloSubscription(
    val feedId: String,
    val category: String? = null,
    val title: String? = null,
    val feeds: Feed? = null,
    val createdAt: String, // "2024-12-10T15:49:20.231Z"
)

@Serializable
data class Feed(
    val id: String? = null,
    val url: String? = null,
    val siteUrl: String? = null,
    val image: String? = null,
)
