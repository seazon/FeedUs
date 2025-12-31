package com.seazon.feedme.lib.rss.service.folo

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.folo.api.AuthenticationApi
import com.seazon.feedme.lib.rss.service.folo.api.DiscoverApi
import com.seazon.feedme.lib.rss.service.folo.api.MainApi

class FoloApi : RssApi {

    private var token: RssToken? = null
    private val authenticationApi: AuthenticationApi
    private var mainApi: MainApi? = null
    var discoverApi: DiscoverApi? = null

    init {
        authenticationApi = AuthenticationApi()
    }

    override fun setToken(token: RssToken) {
        this.token = token
        mainApi = MainApi(token)
        discoverApi = DiscoverApi(token)
    }

    override fun getToken(): RssToken? {
        return token
    }

    override fun getCategoryId(category: String): String? {
        return "user/${token!!.id}/category/${category}"
    }

    /*
     * AuthenticationApi
     */
    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_OAUTH2
    }

    override fun getOAuth2Url(state: String): String {
        return authenticationApi.getOAuth2Url(state)
    }

    override suspend fun getRefreshToken(code: String): String {
        return code
    }

    override suspend fun getAccessToken(token: RssToken): String {
        return token.refreshToken.orEmpty()
    }

    override suspend fun setUserWithAccessToken(token: RssToken, response: String) {
        authenticationApi.applyOneTimeTokenAndSetUser(token, response)
    }

    override suspend fun setUserWithRefreshToken(token: RssToken, response: String) {
        authenticationApi.applyOneTimeTokenAndSetUser(token, response)
    }

    override suspend fun setUserInfo(token: RssToken) {
    }

    /*
     * EntriesApi
     */

    /*
     * MarkersApi
     */
    override suspend fun getUnreadCounts(): RssUnreadCounts? {
        return mainApi?.getUnreadCounts()
    }

    override suspend fun markRead(entryIds: Array<String>?): String? {
        return mainApi?.markArticleRead(entryIds)
    }

    override suspend fun markUnread(entryIds: Array<String>?): String? {
        return null
    }

    override suspend fun markStar(entryIds: Array<String>): String? {
        return mainApi?.markStar(entryIds.first())
    }

    override suspend fun markUnstar(entryIds: Array<String>): String? {
        return mainApi?.markUnstar(entryIds.first())
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    /*
     * StreamsApi
     */
    override fun supportPagingFetchIds(): Boolean {
        return true
    }

    override fun supportFetchByFeed(): Boolean {
        return true
    }

    override suspend fun getStreamByIds(entryIds: Array<String>): RssStream {
        return RssStream()
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream? {
        return RssStream()
    }

    override suspend fun getFeedStreamIds(
        feedId: String,
        count: Int,
        continuation: String?
    ): RssStream? {
        return null
    }

    override suspend fun getCategoryStreamIds(
        category: String,
        count: Int,
        continuation: String?
    ): RssStream? {
        return null
    }

    override suspend fun getUnraedStream(
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getEntriesForAll(count, continuation)
    }

    override suspend fun getFeedStream(
        feedId: String,
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getEntriesForFeed(feedId, count, continuation)
    }

    override suspend fun getCategoryStream(
        category: String, // folo not support, need put feedId list json here
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getEntriesForCategory(category, count, continuation)
    }

    override suspend fun getTagStreamIds(
        tag: String,
        count: Int,
        continuation: String?
    ): RssStream? {
        return RssStream()
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream? {
        return mainApi?.getEntriesForCollection(count, continuation)
    }

    override fun supportStar(): Boolean {
        return true
    }

    /*
     * SubscriptionsApi
     */
    override suspend fun getSubscriptions(): List<RssFeed>? {
        return mainApi?.getSubscriptions()
    }

    override fun supportSubscribe() = true

    override fun supportUpdateSubscription() = false

    override suspend fun subscribeFeed(
        title: String,
        feedId: String, // here is feed url
        categories: Array<String>
    ): Boolean {
        val response = mainApi?.subscribeFeed(title, feedId, categories.firstOrNull { !it.isNullOrEmpty() })
        return response?.feed != null
    }

    override suspend fun unsubscribeFeed(feedId: String): String? {
        return mainApi?.unsubscribeFeed(feedId)
    }

    override suspend fun editFeed(
        title: String,
        feedId: String,
        aCategories: Array<String>,
        rCategories: Array<String>
    ): String? {
        return null
    }

    /*
     * TagsApi
     */
    override fun supportCreateTag(): Boolean {
        return false
    }

    override suspend fun getTags(): List<RssTag>? {
        return null
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
    }

}
