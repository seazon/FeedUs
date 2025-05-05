package com.seazon.feedme.lib.rss.service.feedbin.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedbin.FeedbinConstants
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinCategory
import com.seazon.feedme.lib.utils.jsonOf

class TaggingsApi(token: RssToken) : AuthedApi(token) {

    suspend fun getTaggings(): List<FeedbinCategory>? {
        return execute(HttpMethod.GET, FeedbinConstants.URL_TAGGINGS).convertBody()
    }

    suspend fun createTagging(feedId: Int, name: String?): String? {
        val o = jsonOf(
            "feed_id" to feedId,
            "name" to name,
        )
        return execute(HttpMethod.POST, FeedbinConstants.URL_TAGGINGS, null, null, o.toString()).body
    }

    suspend fun deleteTagging(taggingId: Int): String? {
        return execute(HttpMethod.DELETE, String.format(FeedbinConstants.URL_TAGGING, taggingId)).body
    }
}
