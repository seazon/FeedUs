package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.urlEncode
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlyTag
import com.seazon.feedme.lib.utils.jsonOf
import io.ktor.client.call.body

class TagsApi(feedlyToken: RssToken) : AuthedApi(feedlyToken) {

    suspend fun getTags(): List<FeedlyTag>? {
        return execute(HttpMethod.GET, FeedlyConstants.URL_TAGS).body()
    }

    suspend fun tagEntry(entryId: String?, tagIds: Array<String?>?): String? {
        if (entryId.isNullOrEmpty() || tagIds.isNullOrEmpty()) {
            return null
        }

        var tagIdsAll: String? = null
        for (i in tagIds.indices) {
            if (tagIds[i].isNullOrEmpty()) {
                continue
            }

            if (tagIdsAll == null) {
                tagIdsAll = "user/${token.id}/tag/${tagIds[i]?.urlEncode()}"
            } else {
                tagIdsAll += (",user/${token.id}/tag/${tagIds[i]?.urlEncode()}")
            }
        }

        val o = jsonOf(
            "entryId" to entryId,
        )

        if (tagIdsAll == null) {
            return null
        }

        return execute(HttpMethod.PUT, FeedlyConstants.URL_TAGS + "/" + tagIdsAll, null, null, o.toString()).body()
    }

    suspend fun tagEntries(entryIds: Array<String>, tagIds: Array<String>): String {
        if (entryIds.isEmpty() || tagIds.isEmpty()) {
            return ""
        }

        var tagIdsAll: String? = null
        for (i in tagIds.indices) {
            if (tagIds[i].isNullOrEmpty()) {
                continue
            }

            if (tagIdsAll == null) {
                tagIdsAll = "user/" + token.id + "/tag/" + tagIds[i].urlEncode()
            } else {
                tagIdsAll += (","
                        + "user/" + token.id + "/tag/" + tagIds[i].urlEncode())
            }
        }

        val o = jsonOf(
            "entryIds" to entryIds.mapNotNull { it },
        )

        if (tagIdsAll == null) {
            return ""
        }

        return execute(HttpMethod.PUT, FeedlyConstants.URL_TAGS + "/" + tagIdsAll, null, null, o.toString()).body()
    }

    suspend fun untagEntries(entryIds: Array<String>, tagIds: Array<String>): String? {
        if (entryIds.isEmpty() || tagIds.isEmpty()) {
            return null
        }

        var entryIdsAll: String? = null
        var tagIdsAll: String? = null
        for (i in entryIds.indices) {
            if (entryIds[i].isNullOrEmpty()) {
                continue
            }

            if (entryIdsAll == null) {
                entryIdsAll = entryIds[i].urlEncode()
            } else {
                entryIdsAll += "," + entryIds[i].urlEncode()
            }
        }
        for (i in tagIds.indices) {
            if (tagIds[i].isNullOrEmpty()) {
                continue
            }

            if (tagIdsAll == null) {
                tagIdsAll = "user/" + token.id + "/tag/" + tagIds[i].urlEncode()
            } else {
                tagIdsAll += ("," + "user/" + token.id + "/tag/" + tagIds[i].urlEncode())
            }
        }

        if (tagIdsAll == null || entryIdsAll == null) {
            return null
        }

        return execute(HttpMethod.DELETE, FeedlyConstants.URL_TAGS + "/" + tagIdsAll + "/" + entryIdsAll).body()
    }

    suspend fun deleteTags(tagIds: Array<String>): String? {
        if (tagIds.isEmpty()) {
            return null
        }

        var tagIdsAll: String? = null
        for (i in tagIds.indices) {
            if (tagIds[i].isNullOrEmpty()) {
                continue
            }

            if (tagIdsAll == null) {
                tagIdsAll = "user/" + token.id + "/tag/" + tagIds[i].urlEncode()
            } else {
                tagIdsAll += ("," + "user/" + token.id + "/tag/" + tagIds[i].urlEncode())
            }
        }

        if (tagIdsAll == null) {
            return null
        }

        return execute(HttpMethod.DELETE, FeedlyConstants.URL_TAGS + "/" + tagIdsAll).body()
    }
}
