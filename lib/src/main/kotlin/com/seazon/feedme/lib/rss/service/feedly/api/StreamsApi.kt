package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlyStream
import io.ktor.client.call.body

class StreamsApi(feedlyToken: RssToken) : AuthedApi(feedlyToken) {

    suspend fun getIds(streamId: String, count: Int, unreadOnly: Boolean, continuation: String?): FeedlyStream? {
        var count = count
        if (count <= 0) {
            count = 20
        }
        if (count > 1000) {
            count = 1000
        }
        val parameters = listOf(
            NameValuePair("count", count.toString()),
            NameValuePair("unreadOnly", unreadOnly.toString()),
            NameValuePair("newerThan", "0"),
            NameValuePair("continuation", continuation.orEmpty()),
            NameValuePair("streamId", streamId),
        )
        return execute(HttpMethod.GET, FeedlyConstants.URL_STREAMS_IDS, parameters).body()
    }

    suspend fun getContents(
        streamId: String,
        count: Int,
        unreadOnly: Boolean,
        newerThan: String?,
        continuation: String?,
    ): FeedlyStream? {
        var count = count
        if (count <= 0) {
            count = 20
        }
        if (count > 1000) {
            count = 1000
        }
        val parameters = listOf(
            NameValuePair("count", count.toString()),
            NameValuePair("unreadOnly", unreadOnly.toString()),
            NameValuePair("newerThan", newerThan.orEmpty()),
            NameValuePair("continuation", continuation.orEmpty()),
            NameValuePair("streamId", streamId),
        )

        return execute(HttpMethod.GET, FeedlyConstants.URL_STREAMS_CONTENTS, parameters).body()
    }
}
