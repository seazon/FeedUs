package com.seazon.feedme.lib.rss.service.fever

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.SelfHostedRssApi
import com.seazon.feedme.lib.rss.service.fever.api.AuthenticationApi
import com.seazon.feedme.lib.rss.service.fever.api.MainApi
import com.seazon.feedme.lib.rss.service.fever.bo.Feeds
import com.seazon.feedme.lib.rss.service.fever.bo.FeverStream
import com.seazon.feedme.lib.rss.service.fever.bo.Groups
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class FeverApi(var _token: RssToken) : RssApi, SelfHostedRssApi {

    private var mainApi: MainApi? = null
    private val authenticationApi by lazy { AuthenticationApi(_token) }

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

    override fun getToken(): RssToken? {
        return _token
    }

    override fun setToken(token: RssToken) {
        this._token = token
        mainApi = MainApi(token)
    }

    override fun getOAuth2Url(state: String): String {
        return ""
    }

    override suspend fun getRefreshToken(code: String): String {
        return ""
    }

    override suspend fun setUserWithRefreshToken(token: RssToken, response: String) {
    }

    override suspend fun getAccessToken(token: RssToken): String {
        return authenticationApi.getAccessToken(token.username, token.password)
    }

    override suspend fun setUserWithAccessToken(token: RssToken, response: String) {
        authenticationApi.setUserWithAccessToken(token, response)
    }

    override suspend fun setUserInfo(token: RssToken) {
        token.email = token.username
    }

    override fun supportPagingFetchIds(): Boolean {
        return false
    }

    override fun supportFetchByFeed(): Boolean {
        return false
    }

    override fun getCategoryId(category: String): String {
        return ""
    }

    override fun supportUnreadCounts() = false

    override suspend fun markRead(entryIds: Array<String>?): String? {
        entryIds?.forEach {
            it?.run {
                mainApi?.markItem(this, "read")
            }
        }
        return ""
    }

    override suspend fun markUnread(entryIds: Array<String>?): String? {
        return ""
    }

    override suspend fun getStreamByIds(entryIds: Array<String>): RssStream {
        val chunks = entryIds.asList().chunked(FeverConstants.FETCH_ITEM_IDS_MAX_COUNT)
        val allItems = coroutineScope {
            chunks.map { chunk ->
                async {
                    FeverStream.parse(mainApi?.getItems(chunk.toTypedArray())).items
                }
            }.awaitAll()
        }.flatten()
        return RssStream().apply {
            items = allItems.sortedByDescending { it.publisheddate }
        }
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream {
        return FeverStream.parse(mainApi?.getUnreadItemIds())
    }

    override suspend fun getUnraedStream(count: Int, since: String?, continuation: String?): RssStream {
        val idsRssStream = getUnraedStreamIds(count, continuation)
        return getStreamByIds(idsRssStream.ids.take(count).toTypedArray()).apply {
            items = items.sortedByDescending { it.publisheddate }
        }
    }

    override suspend fun getSubscriptions(): List<RssFeed> {
        return Feeds.parse(mainApi?.getFeeds(), Groups.parse(mainApi?.getGroups()))
    }

    override fun supportSubscribe(): Boolean {
        return false
    }

    override suspend fun subscribeFeed(title: String, feedId: String?, feedUrl: String?, categories: Array<String>): Boolean {
        return false
    }

    override suspend fun unsubscribeFeed(feedId: String): String {
        return ""
    }

    override suspend fun editFeed(
        title: String,
        feedId: String,
        aCategories: Array<String>,
        rCategories: Array<String>
    ): String {
        return ""
    }

    override fun supportStar(): Boolean {
        return true
    }

    override suspend fun markStar(entryIds: Array<String>): String {
        entryIds?.forEach {
            it?.run {
                mainApi?.markItem(this, "saved")
            }
        }
        return ""
    }

    override suspend fun markUnstar(entryIds: Array<String>): String {
        entryIds?.forEach {
            it?.run {
                mainApi?.markItem(this, "unsaved")
            }
        }
        return ""
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream {
        return FeverStream.parse(mainApi?.getSavedItemIds())
    }

    override fun supportCreateTag(): Boolean {
        return true
    }

    override suspend fun getTags(): List<RssTag> {
        return ArrayList()
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    override suspend fun getTagStreamIds(tag: String, count: Int, continuation: String?): RssStream {
        return RssStream()
    }

}