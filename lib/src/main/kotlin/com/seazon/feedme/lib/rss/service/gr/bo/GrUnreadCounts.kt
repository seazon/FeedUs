package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.RssUnreadCount
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.utils.orZero
import kotlinx.serialization.Serializable
import kotlin.collections.orEmpty

@Serializable
data class GrUnreadCounts(
    var max: Int = 0,
    var unreadcounts: MutableList<GrUnreadCount>? = null,
) {
    fun convert(): RssUnreadCounts {
        return RssUnreadCounts(
            max = max,
            unreadCounts = unreadcounts?.map {
                RssUnreadCount(
                    id = it.id,
                    count = it.count  ,
                    updated = try {
                        it.newestItemTimestampUsec?.toLong().orZero()
                    } catch (e: Exception) {
                        0
                    },
                )
            }.orEmpty()
        )
    }
}