package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class FoloFeeds(
    val feed: Feed? = null,
    val entries: List<Entry>? = null,
) {
    @Serializable
    data class Entry(
        val title: String? = null,
        val url: String? = null,
        val content: String? = null,
        val description: String? = null,
        val guid: String? = null,
        val author: String? = null,
        val publishedAt: String? = null,
    )
}

