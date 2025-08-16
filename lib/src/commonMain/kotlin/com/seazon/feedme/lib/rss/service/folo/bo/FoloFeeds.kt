package com.seazon.feedme.lib.rss.service.folo.bo

import com.seazon.feedme.lib.rss.bo.RssStream
import kotlinx.serialization.Serializable

@Serializable
data class FoloFeeds(
    val feed: FoloFeed? = null,
    val entries: List<FoloEntry>? = null,
) {
    fun convert(): RssStream {
        return RssStream(
            items = entries?.map { entry ->
                entry.convert(
                    feed = feed,
                    category = null,
                    read = true,
                    collections = null
                ) // TODO check read state
            }.orEmpty()
        )
    }
}

