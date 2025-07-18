package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants

//TODO now category data fetch via getSubscriptions()
class CategoriesApi(feedlyToken: RssToken) : AuthedApi(feedlyToken) {

    suspend fun getCategories(): String? {
        return execute(HttpMethod.GET, FeedlyConstants.URL_CATEGORIES).body
    }
}
