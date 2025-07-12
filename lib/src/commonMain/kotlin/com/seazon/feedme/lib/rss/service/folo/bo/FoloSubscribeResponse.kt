package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class FoloSubscribeResponse(
    val feed: FoloFeed? = null,
)