package com.seazon.feedus.ui.articles

import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.Item

data class ArticlesScreenState(
    val isLoading: Boolean = false,
    val hasMore: Boolean = true,
    val categoryId: String? = null,
    val feedId: String? = null,
    val starred: Boolean = false,
    val title: String? = null,
    val listType: ListType = ListType.NORMAL,
    val items: List<Item> = emptyList(),
    val continuation: String? = null,
    val feedMap: Map<String, Feed> = emptyMap(),
)

enum class ListType {
    NORMAL, STARRED, TAG
}
