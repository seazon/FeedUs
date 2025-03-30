package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val id: String, // feed/[feedurl]
    var title: String? = null,
    var sortId: String? = "",
    var url: String? = null, // website url
    var feedUrl: String? = null,  // rss url
    var categories: String? = null,
    var favicon: String? = null,
    var cntClientAll: Int = 0,
    var cntClientUnread: Int = 0,
)