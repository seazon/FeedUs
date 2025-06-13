package com.seazon.feedme.lib.rss.service.folo.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.toType
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCount
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.folo.FoloConstants
import com.seazon.feedme.lib.rss.service.folo.bo.FoloData
import com.seazon.feedme.lib.rss.service.folo.bo.FoloEntries
import com.seazon.feedme.lib.rss.service.folo.bo.FoloFeeds
import com.seazon.feedme.lib.rss.service.folo.bo.FoloListData
import com.seazon.feedme.lib.rss.service.folo.bo.FoloSubscription
import com.seazon.feedme.lib.utils.jsonOf
import com.seazon.feedme.lib.utils.toJson
import io.ktor.client.call.body

class MainApi(token: RssToken) : AuthedApi(token) {

    suspend fun getSubscriptions(): List<RssFeed>? {
        val data = execute(
            HttpMethod.GET, FoloConstants.URL_SUBSCRIPTIONS
        ).toType<FoloListData<FoloSubscription>>()
        return data?.data?.map {
            RssFeed(
                id = it.feedId,
                title = if (it.title.isNullOrEmpty()) it.feeds?.title else it.title,
//                title = if (it.title.isNullOrEmpty()) "${it.viewString}${it.feeds?.title}" else "${it.viewString}${it.title}",
                url = it.feeds?.siteUrl,
                feedUrl = it.feeds?.url,
                categories = listOfNotNull(if (it.category.isNullOrEmpty()) null else RssTag(it.category, it.category)),
                favicon = it.feeds?.image,
            )
        }
    }

    suspend fun getContents(id: String): RssStream? {
        val parameters = listOf(
            NameValuePair("id", id),
            NameValuePair("entriesLimit", "10"),
        )
        val data = execute(
            HttpMethod.GET,
            FoloConstants.URL_FEEDS,
            parameters,
        ).toType<FoloData<FoloFeeds>>()
        return data?.data?.convert()
    }

    suspend fun getEntriesForAll(limit: Int, publishedAfter: String?): RssStream? {
        return getEntries(limit, publishedAfter)
    }

    suspend fun getEntriesForCollection(limit: Int, publishedAfter: String?): RssStream? {
        return getEntries(limit, publishedAfter, arrayOf("isCollection" to true))
    }

    suspend fun getEntriesForFeed(feedId: String, limit: Int, publishedAfter: String?): RssStream? {
        return getEntries(limit, publishedAfter, arrayOf("feedId" to feedId))
    }

    suspend fun getEntriesForCategory(category: String, limit: Int, publishedAfter: String?): RssStream? {
        val feedIds = toJson<Array<String>>(category)
        return getEntries(limit, publishedAfter, arrayOf("feedIdList" to feedIds))
    }

    private suspend fun getEntries(
        limit: Int,
        publishedAfter: String?,
        array: Array<Pair<String, Any?>> = emptyArray()
    ): RssStream? {
        val o = jsonOf(
            "read" to false,
//                "view" to 0, // if view is not right, won't return data
            "withContent" to true,
            "limit" to limit,
            *array,
            *if (!publishedAfter.isNullOrEmpty()) arrayOf("publishedAfter" to publishedAfter) else emptyArray()
        )
        val data =
            execute(HttpMethod.POST, FoloConstants.URL_ENTRIES, body = o.toString()).toType<FoloListData<FoloEntries>>()
        val items = data?.data?.mapNotNull { entries ->
            entries.convert()
        }.orEmpty()
        return RssStream(
            items = items,
            continuation = data?.data?.lastOrNull()?.entries?.publishedAt
        )
    }

    suspend fun getUnreadCounts(): RssUnreadCounts? {
        val data = execute(HttpMethod.GET, FoloConstants.URL_READS).toType<FoloData<Map<String, Int>>>()
        return RssUnreadCounts(
            unreadCounts = data?.data?.entries?.map {
                RssUnreadCount(
                    id = it.key,
                    count = it.value,
                )
            }.orEmpty()
        )
    }

    suspend fun markArticleRead(entryIds: Array<String>?): String? {
        val o = jsonOf(
            "entryIds" to entryIds?.mapNotNull { it },
            "isInbox" to false,
            "readHistories" to entryIds?.mapNotNull { it },
        )
        return execute(HttpMethod.POST, FoloConstants.URL_READS, null, null, o.toString()).body()
    }
}