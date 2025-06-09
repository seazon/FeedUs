package com.seazon.feedme.lib.rss.service.folo.api

import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.toType
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.folo.FoloConstants
import com.seazon.feedme.lib.rss.service.folo.bo.FoloData
import com.seazon.feedme.lib.rss.service.folo.bo.FoloListData
import com.seazon.feedme.lib.rss.service.folo.bo.FoloFeeds
import com.seazon.feedme.lib.rss.service.folo.bo.FoloSubscription

class MainApi(token: RssToken) : AuthedApi(token) {

    suspend fun getSubscriptions(): List<RssFeed>? {
        val data = execute(
            HttpMethod.GET,
            FoloConstants.URL_SUBSCRIPTIONS
        ).toType<FoloListData<FoloSubscription>>()
        return data?.data?.map {
            RssFeed(
                id = it.feedId,
                title = it.title,
                url = it.feeds?.siteUrl,
                feedUrl = it.feeds?.url,
                categories = listOf(RssTag(it.category, it.category)),
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
        return RssStream(
            items = data?.data?.entries?.map { entry ->
                RssItem(
                    id = entry.guid,
                    fid = data.data.feed?.id,
                    title = entry.title,
                    link = entry.url,
                    author = entry.author,
                    publisheddate = 0, // entry.publishedAt,
                    updateddate = 0,
                    description = entry.content,
                    tags = null,
                    visual = null,
                    isUnread = true,// TODO
                )
            }.orEmpty()
        )

    }
}