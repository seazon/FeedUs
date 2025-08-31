package com.seazon.feedme.lib.rss.service

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts

interface RssApi {
    /*
     * AuthenticationApi
     */
    fun getAuthType(): Int

    fun getToken(): RssToken?

    fun setToken(token: RssToken)

    /**
     * 获取oauth2的url，用于oauth2
     */
    fun getOAuth2Url(state: String): String? = null

    /**
     * 首次获取access token和refresh token，用于oauth2
     */
    suspend fun getRefreshToken(code: String): String? = null

    /**
     * 首次保存access token和refresh token，用于oauth2
     */
    suspend fun setUserWithRefreshToken(token: RssToken, response: String)

    /**
     * 获取 access token
     */
    suspend fun getAccessToken(token: RssToken): String? = null

    /**
     * 保存 access token
     */
    suspend fun setUserWithAccessToken(token: RssToken, response: String)

    /**
     * 可选
     */
    suspend fun setUserInfo(token: RssToken)
    /*
     * EntriesApi
     * ================
     */
    /**
     * 表明此服务是否支持分页抓取ids。
     */
    fun supportPagingFetchIds(): Boolean

    /**
     * 表明此服务是否支持按feed和按category抓取。如果不支持，则只能整体抓取。
     */
    fun supportFetchByFeed(): Boolean

    fun getCategoryId(category: String): String? = null

    fun supportUnreadCounts() = true
    /**
     * if supportPagingFetchIds=false, not need to implement this
     */
    suspend fun getUnreadCounts(): RssUnreadCounts? = null

    suspend fun markRead(entryIds: Array<String>?): String?

    suspend fun markUnread(entryIds: Array<String>?): String?

    suspend fun getStreamByIds(entryIds: Array<String>): RssStream?

    /**
     * 获取所有未读条目
     */
    suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream?

    suspend fun getUnraedStream(count: Int, since: String?, continuation: String?): RssStream?

    /**
     * 获取一个订阅源的所有未读条目
     * if supportPagingFetchIds=false, not need to implement this
     */
    suspend fun getFeedStreamIds(feedId: String, count: Int, continuation: String?): RssStream? = null

    suspend fun getFeedStream(feedId: String, count: Int, since: String?, continuation: String?): RssStream? = null

    /**
     * 获取一个category的所有未读条目
     * if supportPagingFetchIds=false, not need to implement this
     */
    suspend fun getCategoryStreamIds(category: String, count: Int, continuation: String?): RssStream? = null

    suspend fun getCategoryStream(category: String, count: Int, since: String?, continuation: String?): RssStream? = null

    /*
     * SubscriptionsApi
     * ================
     */
    suspend fun getSubscriptions(): List<RssFeed>?

    /**
     * support subscribe/unsubscribe
     */
    fun supportSubscribe(): Boolean

    /**
     * support update subscription
     */
    fun supportUpdateSubscription(): Boolean = false

    /**
     * 由于最早支持的是feedly，而所有的源搜索都来自feedly的所有接口，
     * 所以传入的feedId都是以feedly的feed id作为标准，即：feed/{feed_url}
     */
    suspend fun subscribeFeed(title: String, feedId: String?, feedUrl: String?, categories: Array<String>): Boolean

    suspend fun unsubscribeFeed(feedId: String): String?

    suspend fun editFeed(title: String, feedId: String, aCategories: Array<String>, rCategories: Array<String>): String? = null

    /*
     * StarApi
     * ================
     */
    fun supportStar(): Boolean

    suspend fun markStar(entryIds: Array<String>): String?

    suspend fun markUnstar(entryIds: Array<String>): String?

    /**
     * 获取加星的所有条目
     */
    suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream?

    /*
     * TagsApi
     * ================
     */
    fun supportCreateTag(): Boolean

    suspend fun getTags(): List<RssTag>?

    suspend fun deleteTags(tagIds: Array<String>)

    suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>)

    suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>)

    /**
     * fetch all items of one tag
     */
    suspend fun getTagStreamIds(tag: String, count: Int, continuation: String?): RssStream? = null

    companion object {
        const val AUTH_TYPE_NONE = 1
        const val AUTH_TYPE_BASE = 2
        const val AUTH_TYPE_OAUTH2 = 3
        const val HTTP_CODE_BAD_REQUEST = 400
        const val HTTP_CODE_UNAUTHORIZED = 401
    }
}