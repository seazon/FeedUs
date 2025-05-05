package com.seazon.feedme.lib.rss.service.feedbin.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedbin.FeedbinConstants

class EntriesApi(token: RssToken) : AuthedApi(token) {
    /**
     * @param ids 上限一百条
     */
    suspend fun getEntries(appointed: Boolean, ids: Array<String>?, count: Int, since: String?): String? {
        var since = since
        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        if (appointed) {
            if (ids.isNullOrEmpty() || ids.size > 100) {
                return null
            }
            val idsString = StringBuilder()
            for (i in ids.indices) {
                if (i == 0) {
                    idsString.append(ids[i])
                } else {
                    idsString.append(",").append(ids[i])
                }
            }
            parameters.add(NameValuePair("ids", idsString.toString()))
            parameters.add(NameValuePair("per_page", ids.size.toString()))
        } else {
            parameters.add(NameValuePair("per_page", count.toString()))
        }
        if (since != null) {
            if (since == "0") {
                since = "1980-01-01T00:00:00.000000Z"
            }
            parameters.add(NameValuePair("since", since))
        }
        parameters.add(NameValuePair("include_enclosure", "true"))
        parameters.add(NameValuePair("mode", "extended"))
        return execute(HttpMethod.GET, FeedbinConstants.URL_ENTRIES, parameters, null, null).body
    }

    suspend fun getFeedEntries(feedId: String, appointed: Boolean, ids: Array<String>?, count: Int, since: String?): String? {
        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        if (appointed) {
            if (ids == null || ids.size == 0 || ids.size > 100) {
                return null
            }
            val idsString = StringBuilder()
            for (i in ids.indices) {
                if (i == 0) {
                    idsString.append(ids[i])
                } else {
                    idsString.append(",").append(ids[i])
                }
            }
            parameters.add(NameValuePair("ids", idsString.toString()))
            parameters.add(NameValuePair("per_page", ids.size.toString()))
        } else {
            parameters.add(NameValuePair("per_page", count.toString()))
        }
        if (since != null) {
            parameters.add(NameValuePair("since", since))
        }
        parameters.add(NameValuePair("include_enclosure", "true"))
        parameters.add(NameValuePair("mode", "extended"))
        return execute(HttpMethod.GET, String.format(FeedbinConstants.URL_FEED_ENTRIES, feedId), parameters, null, null).body
    }
}
