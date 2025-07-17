package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.RssUnreadCount
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.utils.IntAsStringSerializer
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable

@Serializable
data class TtrssCounterList(
    var content: List<TtrssCounter>? = null,
) {
    companion object {
        fun parse(json: String?): RssUnreadCounts {
            val list = toJson<TtrssCounterList>(json.orEmpty())
            return RssUnreadCounts(
                unreadCounts = list.content?.filter { it.kind != "cat" }?.map {
                    RssUnreadCount(
                        id = it.id,
                        count = it.counter,
                    )
                }.orEmpty()
            )
        }
    }
}

@Serializable
data class TtrssCounter(
    @Serializable(with = IntAsStringSerializer::class)
    val id: String? = null,
    val counter: Int = 0,
    val kind: String? = null,
)
