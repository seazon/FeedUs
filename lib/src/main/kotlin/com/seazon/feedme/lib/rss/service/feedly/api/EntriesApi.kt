package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlyItem
import io.ktor.client.call.body
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EntriesApi(feedlyToken: RssToken) : AuthedApi(feedlyToken) {

    suspend fun getContent(entryIds: Array<String>?): List<FeedlyItem>? {
        if (entryIds.isNullOrEmpty()) {
            return null
        }
        return execute(HttpMethod.POST, FeedlyConstants.URL_ENTRIES, null, null, Json.encodeToString(entryIds)).body()
    }
}
