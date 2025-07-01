package com.seazon.feedme.lib.rss.service.feedbin

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.SelfHostedRssApi
import com.seazon.feedme.lib.rss.service.feedbin.api.AuthenticationApi
import com.seazon.feedme.lib.rss.service.feedbin.api.EntriesApi
import com.seazon.feedme.lib.rss.service.feedbin.api.StarredEntriesApi
import com.seazon.feedme.lib.rss.service.feedbin.api.SubscriptionsApi
import com.seazon.feedme.lib.rss.service.feedbin.api.TaggingsApi
import com.seazon.feedme.lib.rss.service.feedbin.api.UnreadEntriesApi
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinStream
import com.seazon.feedme.lib.rss.service.feedbin.bo.convert

class FeedbinApi(var _token: RssToken) : RssApi, SelfHostedRssApi {
    protected var authenticationApi: AuthenticationApi
    protected var subscriptionsApi: SubscriptionsApi? = null
    protected var unreadEntriesApi: UnreadEntriesApi? = null
    protected var starredEntriesApi: StarredEntriesApi? = null
    protected var entriesApi: EntriesApi? = null
    protected var taggingsApi: TaggingsApi? = null

    init {
        authenticationApi = AuthenticationApi(_token)
    }

    override fun getToken(): RssToken? {
        return _token
    }

    override fun setToken(token: RssToken) {
        this._token = token
        subscriptionsApi = SubscriptionsApi(token)
        unreadEntriesApi = UnreadEntriesApi(token)
        starredEntriesApi = StarredEntriesApi(token)
        entriesApi = EntriesApi(token)
        taggingsApi = TaggingsApi(token)
    }

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
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

    override suspend fun markRead(entryIds: Array<String>?): String? {
        return unreadEntriesApi?.deleteUnreadEntries(entryIds)
    }

    override suspend fun markUnread(entryIds: Array<String>?): String? {
        return unreadEntriesApi?.createUnreadEntries(entryIds)
    }

    override suspend fun markStar(entryIds: Array<String>): String? {
        return starredEntriesApi!!.createStarredEntries(entryIds)
    }

    override suspend fun markUnstar(entryIds: Array<String>): String? {
        return starredEntriesApi!!.deleteStarredEntries(entryIds)
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    override suspend fun getStreamByIds(entryIds: Array<String>): RssStream? {
        return FeedbinStream.parse("{\"items\":" + entriesApi!!.getEntries(true, entryIds, 0, null) + "}")
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream {
        return FeedbinStream(
            ids = unreadEntriesApi?.getUnreadEntries()?.map { it.toString() }
        ).convert()
    }

    override suspend fun getUnraedStream(count: Int, since: String?, continuation: String?): RssStream? {
        return FeedbinStream.parse("{\"items\":" + entriesApi!!.getEntries(false, null, count, since) + "}")
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream {
        return FeedbinStream(
            ids = starredEntriesApi?.getStarredEntries()?.map { it.toString() }
        ).convert()
    }

    override fun supportStar(): Boolean {
        return true
    }

    override suspend fun getSubscriptions(): List<RssFeed>? {
        val subscriptions = subscriptionsApi?.getSubscriptions() ?: return null
        val categories = taggingsApi?.getTaggings() ?: return null
        val maps: MutableMap<String, Int?> = HashMap()
        for (subscription in subscriptions) {
            for (category in categories) {
                if (category.feed_id == subscription.feed_id) {
                    if (maps[category.name] != null) {
                        category.id = maps[category.name]!!
                    } else {
                        maps[category.name.orEmpty()] = category.id.toInt()
                    }
                    subscription.categories?.add(category)
                }
            }
        }
        return subscriptions.convert()
    }

    override fun supportSubscribe(): Boolean {
        return true
    }

    override suspend fun subscribeFeed(title: String, feedId: String, categories: Array<String>): Boolean {
        val url = RssApi.id2url(feedId)
        subscriptionsApi?.createSubscriptions(url)
        return true // TODO should check response for error case
    }

    override suspend fun unsubscribeFeed(id: String): String? {
        val subscriptions = subscriptionsApi?.getSubscriptions()
        if (subscriptions != null && subscriptions.size > 0) {
            for (s in subscriptions) {
                if (id == s.getId()) {
                    return subscriptionsApi!!.deleteSubscriptions(s.realId.toString())
                }
            }
        }
        throw HttpException.getInstance(Exception("can't find feed"))
    }

    override fun supportCreateTag(): Boolean {
        return true
    }

    override suspend fun getTags(): List<RssTag> {
        return ArrayList()
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
    }

    override fun getDefaultHost(): String {
        return FeedbinConstants.SCHEMA_HTTPS
    }
}
