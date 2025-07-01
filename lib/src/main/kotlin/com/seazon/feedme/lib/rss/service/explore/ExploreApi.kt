package com.seazon.feedme.lib.rss.service.explore

import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.rss.service.feedly.api.SearchApi
import com.seazon.feedme.lib.rss.service.folo.FoloApi

object ExploreApi {

    suspend fun search(query: String, api: RssApi?): List<ExploreResult> {
        return when (api?.getToken()?.accoutType) {
            Static.ACCOUNT_TYPE_FOLO -> {
                ExploreResult.parseFoloSearch((api as FoloApi).discoverApi?.findFeeds(query))
            }

            else -> {
                ExploreResult.parseFeedlySearch(SearchApi().findFeeds(query))
            }
        }
    }
}