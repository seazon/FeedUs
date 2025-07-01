package com.seazon.feedus.ui.feeds

import com.seazon.feedme.lib.rss.service.explore.ExploreResult

data class SubscribeDialogState(
    val isLoading: Boolean = false,
    val query: String = "", // rss url / keywords
    val errorTips: String = "",
    val results: List<ExploreResult> = emptyList(),
)