package com.seazon.feedme.lib.rss.service.feedbin.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedbin.FeedbinConstants
import com.seazon.feedme.lib.utils.jsonOf
import io.ktor.client.call.body

class UnreadEntriesApi(token: RssToken) : AuthedApi(token) {

    suspend fun getUnreadEntries(): List<Long>? {
        return execute(HttpMethod.GET, FeedbinConstants.URL_UNREAD_ENTRIES).body()
    }

    /**
     * @param ids There is a limit of 1,000 entry_ids per request
     */
    suspend fun createUnreadEntries(ids: Array<String>?): String? {
        val o = jsonOf(
            "unread_entries" to ids?.mapNotNull { it },
        )
        return execute(HttpMethod.POST, FeedbinConstants.URL_UNREAD_ENTRIES, null, null, o.toString()).body()
    }

    /**
     * @param ids There is a limit of 1,000 entry_ids per request
     */
    suspend fun deleteUnreadEntries(ids: Array<String>?): String? {
        val o = jsonOf(
            "unread_entries" to ids?.mapNotNull { it },
        )
        return execute(HttpMethod.DELETE, FeedbinConstants.URL_UNREAD_ENTRIES, null, null, o.toString()).body()
    }
}
