package com.seazon.feedme.lib.rss.service.folo.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.folo.FoloConstants
import com.seazon.feedme.lib.rss.service.folo.bo.DiscoverFeed
import com.seazon.feedme.lib.rss.service.folo.bo.FoloListData
import com.seazon.feedme.lib.utils.jsonOf
import io.ktor.client.call.body

class DiscoverApi(token: RssToken) : AuthedApi(token) {

    /**
     * @param query Can be a feed url, a site title
     */
    suspend fun findFeeds(query: String): FoloListData<DiscoverFeed> {
        val o = jsonOf(
            "keyword" to query,
            "target" to "feeds",
        )
        return execute(HttpMethod.POST, FoloConstants.URL_DISCOVER, body = o.toString())
            .body()
    }
}
