package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverFeed(
    val feed: FoloFeed? = null,
    val entries: List<FoloEntry>? = null,
    val subscriptionCount: Int? = null,
)
