package com.seazon.feedme.lib.rss.service.ttrss.bo

data class TtrssUnreadCount(
    var id: String? = null,
    var count: Int = 0,
) {
    fun getUpdated(): Long {
        return 0
    }
}
