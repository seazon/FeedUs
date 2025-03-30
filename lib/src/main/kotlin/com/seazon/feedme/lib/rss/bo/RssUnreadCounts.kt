package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class RssUnreadCounts(
    val max: Int = 0,
    @SerialName("unreadcounts")
    val unreadCounts: List<RssUnreadCount> = ArrayList()
) : Entity()