package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverFeed(
    val feed: FoloFeed,
    val entries: List<FoloEntry>?,
    val subscriptionCount: Int,
)
