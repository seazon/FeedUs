package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlyProfile
import io.ktor.client.call.body

class ProfileApi(token: RssToken) : AuthedApi(token) {
    suspend fun getProfile(): FeedlyProfile? {
        return execute(HttpMethod.GET, FeedlyConstants.URL_PROFILE).body()
    }
}
