package com.seazon.feedus.ui.feeds

import com.seazon.feedme.lib.rss.bo.Category
import com.seazon.feedme.lib.rss.bo.Feed

data class FeedsScreenState(
    val isLoading: Boolean = false,
    val serviceName: String = "",
    val maxUnreadCount: Int = 0,
    val starredCount: Int = 0,
    val feeds: List<Feed> = emptyList(),
    val categories: List<Category> = emptyList(),
)