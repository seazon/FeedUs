package com.seazon.feedme.lib.rss.service.folo

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.folo.api.AuthenticationApi
import com.seazon.feedme.lib.rss.service.folo.api.MainApi

class FoloApi : RssApi {

    private var token: RssToken? = null
    private val authenticationApi: AuthenticationApi
    private var mainApi: MainApi? = null

    init {
        authenticationApi = AuthenticationApi()
    }

    override fun setToken(token: RssToken) {
        this.token = token
        mainApi = MainApi(token)
    }

    override fun getToken(): RssToken? {
        return token
    }

    override fun getCategoryId(category: String): String? {
        return "user/" + token!!.id + "/category/" + category
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
//        return authenticationApi.getRefreshToken(code)
        return code
    }

    override suspend fun getAccessToken(token: RssToken): String {
//        authenticationApi.applyOneTimeTokenAndSetUser(token, token.refreshToken)
        return token.refreshToken.orEmpty()
    }

    override suspend fun setUserWithAccessToken(token: RssToken, response: String) {
        authenticationApi.applyOneTimeTokenAndSetUser(token, response)
    }

    override suspend fun setUserWithRefreshToken(token: RssToken, response: String) {
        authenticationApi.applyOneTimeTokenAndSetUser(token, response)
    }

    override suspend fun setUserInfo(token: RssToken) {
//        profileApi?.getProfile()?.let {
//            token.id = it.id
//            token.email = it.email
//            token.picture = it.picture
//        }
    }

    /*
     * EntriesApi
     */

    /*
     * MarkersApi
     */
    override suspend fun getUnreadCounts(): RssUnreadCounts? {
//        return markersApi?.getUnreadCounts()?.convert()
        return null
    }

    override suspend fun markRead(entryIds: Array<String>?): String? {
        return mainApi?.markArticleRead(entryIds)
    }

    override suspend fun markUnread(entryIds: Array<String>?): String? {
//        return markersApi?.keepArticleUnread(entryIds)
        return null
    }

    override suspend fun markStar(entryIds: Array<String>): String? {
//        return tagsApi?.tagEntries(entryIds, arrayOf(tagStarred))
        return null
    }

    override suspend fun markUnstar(entryIds: Array<String>): String? {
//        return tagsApi?.untagEntries(entryIds, arrayOf(tagStarred))
        return null
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
//        tagsApi?.tagEntries(entryIds, tagIds)
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
//        tagsApi?.untagEntries(entryIds, tagIds)
    }

    /*
     * StreamsApi
     */
    override fun supportPagingFetchIds(): Boolean {
        return false
    }

    override fun supportFetchByFeed(): Boolean {
        return true
    }

    override suspend fun getStreamByIds(entryIds: Array<String>): RssStream {
//        return FeedlyStream().apply {
//            items = entriesApi?.getContent(entryIds)
//        }.convert()
        return RssStream()
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream? {
//        return streamsApi?.getIds(
//            "user/" + token!!.id + "/category/" + FeedlyConstants.GLOBAL_CATEGORY_ALL,
//            count, true, continuation
//        )?.convert()
        return RssStream()
    }

    override suspend fun getFeedStreamIds(
        feedId: String,
        count: Int,
        continuation: String?
    ): RssStream? {
//        return streamsApi?.getIds(feedId, count, true, continuation)?.convert()
        return null
    }

    override suspend fun getCategoryStreamIds(
        category: String,
        count: Int,
        continuation: String?
    ): RssStream? {
//        return streamsApi?.getIds(category, count, true, continuation)?.convert()
        return null
    }

    override suspend fun getUnraedStream(
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
//        return streamsApi?.getContents(
//            "user/" + token!!.id + "/category/" + FeedlyConstants.GLOBAL_CATEGORY_ALL,
//            count, true, since, continuation
//        )?.convert()
        return RssStream()
    }

    override suspend fun getFeedStream(
        feedId: String,
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getEntriesForFeed(feedId, count)
    }

    override suspend fun getCategoryStream(
        category: String, // folo not support, need put feedId list json here
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getEntriesForCategory(category, count)
    }

    override suspend fun getTagStreamIds(
        tag: String,
        count: Int,
        continuation: String?
    ): RssStream? {
//        return streamsApi?.getIds(tag, count, false, continuation)?.convert()
        return RssStream()
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream? {
//        return streamsApi?.getIds("user/" + token!!.id + "/tag/" + tagStarred, count, false, continuation)?.convert()
        return RssStream()
    }

    override fun supportStar(): Boolean {
        return true
    }

    /*
     * SubscriptionsApi
     */
    override suspend fun getSubscriptions(): List<RssFeed>? {
//        return subscriptionsApi?.getSubscriptions()?.convert()
        return mainApi?.getSubscriptions()
    }

    override fun supportSubscribe() = true

    override fun supportUpdateSubscription() = true

    override suspend fun subscribeFeed(
        title: String,
        feedId: String,
        categories: Array<String>
    ): Boolean {
//        val response = subscriptionsApi?.subscribeFeed(title, feedId, categories)
//        //        {
////            "errorCode":400, "errorId":"ap5int-sv2.2019123000.2410146", "errorMessage":
////            "invalid feed id"
////        }
//        return response != null && !response.contains("errorMessage")
        return false
    }

    override suspend fun unsubscribeFeed(feedId: String): String? {
//        return subscriptionsApi?.unsubscribeFeed(feedId)
        return null
    }

    override suspend fun editFeed(
        title: String,
        feedId: String,
        aCategories: Array<String>,
        rCategories: Array<String>
    ): String? {
//        return subscriptionsApi?.updateSubscription(feedId, title, aCategories)
        return null
    }

    /*
     * TagsApi
     */
    override fun supportCreateTag(): Boolean {
        return false
    }

    override suspend fun getTags(): List<RssTag>? {
//        return tagsApi?.getTags()?.convert2()
        return null
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
//        tagsApi?.deleteTags(tagIds)
    }

    private val tagStarred: String
        private get() = FoloConstants.GLOBAL_TAG_SAVED


}
