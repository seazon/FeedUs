package com.seazon.feedme.lib.rss.service.fever.api

import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.fever.FeverConstants

class MainApi(token: RssToken) : AuthedApi(token) {

    suspend fun getGroups(): String {
        return execute(FeverConstants.METHOD_GROUPS).body
    }

    suspend fun getFeeds(): String {
        return execute(FeverConstants.METHOD_FEEDS).body
    }

    suspend fun getItems(entryIds: Array<out String>?): String {
        return execute(FeverConstants.METHOD_ITEMS, mutableListOf<NameValuePair>().apply {
            entryIds?.let {
                add(NameValuePair("with_ids", entryIds.joinToString()))
            }
        }).body
    }

    suspend fun getUnreadItemIds(): String {
        return execute(FeverConstants.METHOD_UNREAD_ITEM_IDS).body
    }

    suspend fun getSavedItemIds(): String {
        return execute(FeverConstants.METHOD_SAVED_ITEM_IDS).body
    }

    suspend fun markItem(entryId: String, _as: String): String {
        return execute("", mutableListOf<NameValuePair>().apply {
            add(NameValuePair("mark", "item"))
            add(NameValuePair("as", _as))
            add(NameValuePair("id", entryId))
        }).body
    }

}