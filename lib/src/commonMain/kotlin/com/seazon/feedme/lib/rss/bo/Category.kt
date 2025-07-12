package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val title: String?,
    var sortId: String?,
    var cntClientAll: Int = 0,
    var cntClientUnread: Int = 0,
)