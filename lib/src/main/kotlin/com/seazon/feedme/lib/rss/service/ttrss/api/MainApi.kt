package com.seazon.feedme.lib.rss.service.ttrss.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.ttrss.TtrssConstants
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssStream
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssTagList
import com.seazon.feedme.lib.utils.LogUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class MainApi(token: RssToken) : AuthedApi(token) {

    suspend fun getCounters(): String? {
        val o = mapOf(
            "output_mode" to "f"
        )
        return execute(TtrssConstants.METHOD_GET_COUNTERS, o).body
    }

    suspend fun getCategories(): String? {
        return execute(TtrssConstants.METHOD_GET_CATEGORIES, null).body
    }

    suspend fun getFeeds(): String? {
        val o = mapOf(
            "cat_id" to "-3",
            "unread_only" to "false"
        )
        return execute(TtrssConstants.METHOD_GET_FEEDS, o).body
    }

    suspend fun subscribeToFeed(url: String, categoryId: String): String? {
        val o = mapOf(
            "feed_url" to url,
            "category_id" to categoryId
        )
        return execute(TtrssConstants.METHOD_SUBSCRIBE_TO_FEED, o).body
    }

    suspend fun unsubscribeFeed(feedId: String): String? {
        val o = mapOf(
            "feed_id" to feedId
        )

        return execute(TtrssConstants.METHOD_UNSUBSCRIBE_FEED, o).body
    }

    /**
     * @param articleIds
     * @param mode       0 - set to false, 1 - set to true, 2 - toggle
     * @param field      0 - starred, 2 - unread
     * @return
     * @throws HttpException
     */
    suspend fun updateArticle(articleIds: Array<String>?, mode: Int, field: Int): String? {
        val o = mapOf(
            "article_ids" to articleIds?.joinToString(separator = ",").orEmpty(),
            "mode" to mode,
            "field" to field
        )
        return execute(TtrssConstants.METHOD_UPDATE_ARTICLE, o).body
    }

    suspend fun getLabels(): TtrssTagList? {
        return execute(TtrssConstants.METHOD_GET_LABELS, null).convertBody()
    }

    suspend fun getHeadlines(
        feedId: String,
        isCat: Boolean,
        showContent: Boolean,
        onlyUnread: Boolean,
        count: Int,
        since: String?,
        continuation: String?
    ): String? {
        var count = count
        if (count <= 0) {
            count = 20
        }
        if (count > 200) {
            count = 200
        }
        val o = mapOf(
            "view_mode" to if (onlyUnread) "unread" else "all_articles",
            "show_content" to showContent,
            "limit" to count,
            "feed_id" to feedId,
            "is_cat" to isCat,
            "skip" to continuation.orEmpty(),
            "since_id" to since.orEmpty(),
            "order_by" to "feed_dates",
            "include_attachments" to true
        )
        return execute(TtrssConstants.METHOD_GET_HEADLINES, o).body
    }

    suspend fun getArticle(entryIds: Array<String>?): TtrssStream? {
        if (entryIds == null || entryIds.isEmpty()) {
            return null
        }

        val o = mapOf(
            "article_id" to entryIds.joinToString(separator = ","),
        )
        return execute(TtrssConstants.METHOD_GET_ARTICLE, o).convertBody()
    }

    private fun getStreamId(streamId: String?): String? {
        if (streamId.isNullOrEmpty()) {
            return ""
        }

        try {
            return URLEncoder.encode(streamId, HttpUtils.DEFAULT_CHARSET)
        } catch (e: UnsupportedEncodingException) {
            LogUtils.error(e)
            return streamId
        }
    }

    suspend fun setArticleLabel(assign: Boolean, labelId: String, entryIds: Array<String>?): String? {
        if (entryIds == null || entryIds.isEmpty()) {
            return null
        }

        val o = mapOf(
            "article_ids" to entryIds.joinToString(separator = ","),
            "label_id" to labelId,
            "assign" to assign
        )
        return execute(TtrssConstants.METHOD_SET_ARTICLE_LABEL, o).body
    }
}
