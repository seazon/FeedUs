package com.seazon.feedme.lib.rss.service.gr.bo

import kotlinx.serialization.Serializable

@Serializable
data class GrUnreadCounts(
    var max: Int = 0,
    var unreadcounts: MutableList<GrUnreadCount?>? = null,
)