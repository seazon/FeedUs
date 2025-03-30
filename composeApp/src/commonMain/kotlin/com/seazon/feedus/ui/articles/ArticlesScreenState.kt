package com.seazon.feedus.ui.articles

import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.Item

data class ArticlesScreenState(
    val isLoading: Boolean = false,
    val categoryId: String? = null,
    val feedId: String? = null,
    val title: String? = null,
    val items: List<Item> = emptyList(),
    val feedMap: Map<String, Feed> = emptyMap(),
)