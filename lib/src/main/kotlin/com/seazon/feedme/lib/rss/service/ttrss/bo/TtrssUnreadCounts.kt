package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.RssUnreadCount
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.utils.toJson

data class TtrssCounterList(
    var content: List<TtrssCounter>? = null,
)

data class TtrssCounter(
    var id: String? = null,
    var counter: Int = 0,
)

data class TtrssUnreadCounts(
    var max: Int = 0,
    var unreadcounts: List<TtrssUnreadCount>? = null,
) {
    companion object {
        fun parse(json: String?): RssUnreadCounts {
            val list = toJson<TtrssCounterList>(json.orEmpty())
            return RssUnreadCounts(
                unreadCounts = list.content?.map {
                    RssUnreadCount(
                        id = it.id,
                        count = it.counter,
                    )
                }.orEmpty()
            )
        }
    }
}
