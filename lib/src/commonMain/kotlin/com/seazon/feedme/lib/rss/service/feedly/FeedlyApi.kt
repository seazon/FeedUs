package com.seazon.feedme.lib.rss.service.feedly

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.feedly.api.AuthenticationApi
import com.seazon.feedme.lib.rss.service.feedly.api.EntriesApi
import com.seazon.feedme.lib.rss.service.feedly.api.MarkersApi
import com.seazon.feedme.lib.rss.service.feedly.api.ProfileApi
import com.seazon.feedme.lib.rss.service.feedly.api.StreamsApi
import com.seazon.feedme.lib.rss.service.feedly.api.SubscriptionsApi
import com.seazon.feedme.lib.rss.service.feedly.api.TagsApi
import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlyStream
import com.seazon.feedme.lib.rss.service.feedly.bo.convert
import com.seazon.feedme.lib.rss.service.feedly.bo.convert2

class FeedlyApi : RssApi {
    private var token: RssToken? = null
    private val authenticationApi: AuthenticationApi
    private var entriesApi: EntriesApi? = null
    private var profileApi: ProfileApi? = null
    private var markersApi: MarkersApi? = null
    private var streamsApi: StreamsApi? = null
    private var subscriptionsApi: SubscriptionsApi? = null
    private var tagsApi: TagsApi? = null

    init {
        authenticationApi = AuthenticationApi()
    }

    override fun setToken(token: RssToken) {
        this.token = token
        entriesApi = EntriesApi(token)
        profileApi = ProfileApi(token)
        markersApi = MarkersApi(token)
        streamsApi = StreamsApi(token)
        subscriptionsApi = SubscriptionsApi(token)
        tagsApi = TagsApi(token)
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
        return authenticationApi.getRefreshToken(code)
    }

    override suspend fun getAccessToken(token: RssToken): String {
        return authenticationApi.getAccessToken(token.refreshToken)
    }

    override suspend fun setUserWithAccessToken(token: RssToken, response: String) {
        authenticationApi.setUserWithAccessToken(token, response)
    }

    override suspend fun setUserWithRefreshToken(token: RssToken, response: String) {
        authenticationApi.setUserWithRefreshToken(token, response)
    }

    override suspend fun setUserInfo(token: RssToken) {
        profileApi?.getProfile()?.let {
            token.id = it.id
            token.email = it.email
            token.picture = it.picture
        }
    }

    /*
     * EntriesApi
     */

    /*
     * MarkersApi
     */
    override suspend fun getUnreadCounts(): RssUnreadCounts? {
        return markersApi?.getUnreadCounts()?.convert()
    }

    override suspend fun markRead(entryIds: Array<String>?): String? {
        return markersApi?.markArticleRead(entryIds)
    }

    override suspend fun markUnread(entryIds: Array<String>?): String? {
        return markersApi?.keepArticleUnread(entryIds)
    }

    override suspend fun markStar(entryIds: Array<String>): String? {
        return tagsApi?.tagEntries(entryIds, arrayOf(tagStarred))
    }

    override suspend fun markUnstar(entryIds: Array<String>): String? {
        return tagsApi?.untagEntries(entryIds, arrayOf(tagStarred))
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
        tagsApi?.tagEntries(entryIds, tagIds)
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
        tagsApi?.untagEntries(entryIds, tagIds)
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
        return FeedlyStream().apply {
            items = entriesApi?.getContent(entryIds)
        }.convert()
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream? {
        return streamsApi?.getIds(
            "user/" + token!!.id + "/category/" + FeedlyConstants.GLOBAL_CATEGORY_ALL,
            count, true, continuation
        )?.convert()
    }

    override suspend fun getFeedStreamIds(feedId: String, count: Int, continuation: String?): RssStream? {
        return streamsApi?.getIds(feedId, count, true, continuation)?.convert()
    }

    override suspend fun getCategoryStreamIds(category: String, count: Int, continuation: String?): RssStream? {
        return streamsApi?.getIds(category, count, true, continuation)?.convert()
    }

    override suspend fun getUnraedStream(count: Int, since: String?, continuation: String?): RssStream? {
        return streamsApi?.getContents(
            "user/" + token!!.id + "/category/" + FeedlyConstants.GLOBAL_CATEGORY_ALL,
            count, true, since, continuation
        )?.convert()
    }

    override suspend fun getFeedStream(feedId: String, count: Int, since: String?, continuation: String?): RssStream? {
        return streamsApi?.getContents(feedId, count, true, since, continuation)?.convert()
    }

    override suspend fun getCategoryStream(category: String, count: Int, since: String?, continuation: String?): RssStream? {
        return streamsApi?.getContents(category, count, true, since, continuation)?.convert()
    }

    override suspend fun getTagStreamIds(tag: String, count: Int, continuation: String?): RssStream? {
        return streamsApi?.getIds(tag, count, false, continuation)?.convert()
    }

    override suspend fun getTagStream(tag: String, count: Int, continuation: String?): RssStream? {
        return streamsApi?.getContents(
            "user/${token!!.id}/tag/$tag",
            count, true, null, continuation
        )?.convert()
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream? {
        return streamsApi?.getIds("user/" + token!!.id + "/tag/" + tagStarred, count, false, continuation)?.convert()
    }

    override fun supportStar(): Boolean {
        return true
    }

    /*
     * SubscriptionsApi
     */
    override suspend fun getSubscriptions(): List<RssFeed>? {
        return subscriptionsApi?.getSubscriptions()?.convert()
    }

    override fun supportSubscribe() = true

    override fun supportUpdateSubscription() = true

    override suspend fun subscribeFeed(title: String, feedId: String, categories: Array<String>): Boolean {
        val response = subscriptionsApi?.subscribeFeed(title, feedId, categories)
        //        {
//            "errorCode":400, "errorId":"ap5int-sv2.2019123000.2410146", "errorMessage":
//            "invalid feed id"
//        }
        return response != null && !response.contains("errorMessage")
    }

    override suspend fun unsubscribeFeed(feedId: String): String? {
        return subscriptionsApi?.unsubscribeFeed(feedId)
    }

    override suspend fun editFeed(
        title: String,
        feedId: String,
        aCategories: Array<String>,
        rCategories: Array<String>
    ): String? {
        return subscriptionsApi?.updateSubscription(feedId, title, aCategories)
    }

    /*
     * TagsApi
     */
    override fun supportCreateTag(): Boolean {
        return true
    }

    override suspend fun getTags(): List<RssTag>? {
        return tagsApi?.getTags()?.convert2()
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
        tagsApi?.deleteTags(tagIds)
    }

    private val tagStarred: String
        private get() = FeedlyConstants.GLOBAL_TAG_SAVED
}
