package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.RssUnreadCount
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable

@Serializable
data class TtrssCounterList(
    var content: List<TtrssCounter>? = null,
) {
    companion object {
        fun parse(json: String?): RssUnreadCounts {
            val json2 = json?.replace("\"id\":\"([^\"]*)\"".toRegex(), "\"id\":0")
            val list = toJson<TtrssCounterList>(json2.orEmpty())
            return RssUnreadCounts(
                unreadCounts = list.content?.filter { it.id.orZero() > 0 && it.kind != "cat" }?.map {
                    RssUnreadCount(
                        id = "feed/${it.id.toString()}",
                        count = it.counter,
                    )
                }.orEmpty()
            )
        }
    }
}

@Serializable
data class TtrssCounter(
    val id: Int? = 0, // id可能是int，可能是string，但我们只需要int的
    val counter: Int = 0,
    val kind: String? = null,
)
