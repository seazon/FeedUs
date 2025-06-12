package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class FoloSubscription(
    val feedId: String,
    val view: Int,
    val category: String? = null,
    val title: String? = null,
    val feeds: FoloFeed? = null,
    val createdAt: String, // "2024-12-10T15:49:20.231Z"
) {
    val viewString: String
        get() = when (view) {
            1 -> "[Social]"
            2 -> "[Picture]"
            3 -> "[Video]"
            4 -> "[Audio]"
            5 -> "[Notification]"
            else -> "[Article]"
        }
}

@Serializable
data class FoloFeed(
    val id: String? = null,
    val title: String? = null,
    val url: String? = null,
    val siteUrl: String? = null,
    val image: String? = null,
)
