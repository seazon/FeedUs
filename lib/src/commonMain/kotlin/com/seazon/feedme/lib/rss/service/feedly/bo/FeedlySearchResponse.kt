package com.seazon.feedme.lib.rss.service.feedly.bo

import kotlinx.serialization.Serializable

@Serializable
data class FeedlySearchResponse(
    val results: List<SearchResult>? = null,
) {
    @Serializable
    data class SearchResult(
        val feedId: String? = null,
        val updated: Long? = null,
        val lastUpdated: Long? = null,
        val title: String? = null,
        val subscribers: Int? = null,
        val website: String? = null,
        val description: String? = null,
        val iconUrl: String? = null,
        val visualUrl: String? = null,
    ) {
        val feedUrl: String
            get() = feedId?.substring(5) ?: ""
    }
}
