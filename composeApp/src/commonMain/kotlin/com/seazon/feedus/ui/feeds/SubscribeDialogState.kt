package com.seazon.feedus.ui.feeds

data class SubscribeDialogState(
    val isLoading: Boolean = false,
    val rssUrl: String = "",
    val errorTips: String = "",
)