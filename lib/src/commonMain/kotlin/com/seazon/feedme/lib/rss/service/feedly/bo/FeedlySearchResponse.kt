package com.seazon.feedme.lib.rss.service.feedly.bo

import kotlinx.serialization.Serializable

@Serializable
data class FeedlySearchResponse(
    val results: List<SearchResult>
) {
    @Serializable
    data class SearchResult(
        val feedId: String?,
        val updated: Long?,
        val lastUpdated: Long?,
        val title: String?,
        val subscribers: Int?,
        val website: String?,
        val description: String?,
        val iconUrl: String?,
        val visualUrl: String?
    ) {
        val feedUrl: String
            get() = feedId?.substring(5) ?: ""
    }
}
