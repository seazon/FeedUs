package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class RssUnreadCount(
    val id: String? = null,
    val count: Int = 0,
    @SerialName("newestItemTimestampUsec")
    val updated: Long = 0,
) : Entity()