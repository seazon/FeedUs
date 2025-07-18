package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlySearchResponse

class SearchApi() : BaseApi() {

    /**
     * @param query Can be a feed url, a site title, a site url or a #topic
     */
    suspend fun findFeeds(query: String): FeedlySearchResponse {
        val parameters = listOf(
            NameValuePair("q", query),
            NameValuePair("n", "20"),
        )
        return HttpManager.requestWrap(HttpMethod.GET, getSchema() + FeedlyConstants.URL_SEARCH_FEEDS, parameters)
            .convertBody()
    }
}
