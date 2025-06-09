package com.seazon.feedme.lib.rss.service.localrss

import com.seazon.feedme.lib.rss.service.localrss.bo.LocalRssCategory
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.localrss.bo.LocalRssSubscription
import com.seazon.feedme.lib.utils.LogUtils
import com.seazon.feedme.lib.rss.service.localrss.api.MainApi
import com.seazon.feedme.lib.utils.Helper
import java.util.ArrayList

class LocalRssApi() : RssApi {
    private var token: RssToken? = null
    protected var mainApi: MainApi
    override fun getToken(): RssToken? {
        return token
    }

    override fun setToken(token: RssToken) {
        this.token = token
    }

    override fun getCategoryId(category: String): String {
        return category
    }

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_NONE
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
        return ""
    }

    override suspend fun setUserWithAccessToken(token: RssToken, response: String) {
    }

    override suspend fun setUserInfo(token: RssToken) {
    }

    override fun supportPagingFetchIds(): Boolean {
        return true
    }

    override fun supportFetchByFeed(): Boolean {
        return true
    }

    override suspend fun getUnreadCounts(): RssUnreadCounts {
        return RssUnreadCounts()
    }

    override suspend fun markRead(entryIds: Array<String>?): String? {
        return ""
    }

    override suspend fun markUnread(entryIds: Array<String>?): String {
        return ""
    }

    override suspend fun markStar(entryIds: Array<String>): String {
        return ""
    }

    override suspend fun markUnstar(entryIds: Array<String>): String {
        return ""
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
    }

    override suspend fun getStreamByIds(entryIds: Array<String>): RssStream {
        return RssStream()
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream {
        return RssStream()
    }

    val spiderServiceConnection = SpiderServiceConnection()

    override suspend fun getFeedStreamIds(feedId: String, count: Int, continuation: String?): RssStream? {
        val feed = LocalFeedHelper.getLocalFeedMap()?.get(feedId) ?: return RssStream()
        LogUtils.debug("sync feed:" + feed.feedUrl + ", continuation:" + continuation)
        val stream = spiderServiceConnection.getItems(feed.spiderPackage, feed.feedUrl, continuation)
        if (stream == null) return null
        val size = stream.items.size
        for (i in 0 until size) {
            val item = stream.items[i]
            // 当抓取的文章的时间晚于最后抓取时间，则更新最后抓取时间
            if (item.publishTime > feed.lastClawTime) {
                LogUtils.info("set lastClawTime")
                feed.lastClawTime = item.publishTime
            }
        }
        LocalFeedHelper.saveLocalFeed(feed)

        return RssStream().apply {
            this.continuation = null
            this.items = stream.items.map {
                RssItem().apply {
                    this.id = Helper.md5(feed.id + "/" + it.url)
                    this.fid = feed.id
                    this.title = it.title
                    this.link = it.url
                    this.author = it.author
                    this.publisheddate = it.publishTime
                    this.updateddate = it.publishTime
                    this.description = it.content
                    this.tags = null
                    this.visual = it.thumbnail
                    this.podcastUrl = null
                    this.podcastSize = 0
                    this.feed = feed.convertToRssFeed()
                    this.isUnread = false
                    this.since = null
                }
            }
            this.ids = emptyList()
        }
    }

    override suspend fun getCategoryStreamIds(
        category: String,
        count: Int,
        continuation: String?
    ): RssStream {
        return RssStream()
    }

    override suspend fun getUnraedStream(count: Int, since: String?, continuation: String?): RssStream {
        return RssStream()
    }

    override suspend fun getFeedStream(
        feedId: String,
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream {
        return RssStream()
    }

    override suspend fun getCategoryStream(
        category: String,
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream {
        return RssStream()
    }

    override suspend fun getTagStreamIds(tag: String, count: Int, continuation: String?): RssStream {
        return RssStream()
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream {
        return RssStream()
    }

    override fun supportStar(): Boolean {
        return false
    }

    override suspend fun getSubscriptions(): List<RssFeed> {
        val map = LocalFeedHelper.getLocalFeedMap()
        if (map.isNotEmpty()) {
            val list: MutableList<RssFeed> = ArrayList()
            list.addAll(map.values.map {
                it.convertToRssFeed()
            })
            return list
        }
        return ArrayList()
    }

    suspend fun search(url: String?): LocalRssSubscription? {
        return spiderServiceConnection.getFeed(url.orEmpty())
    }

    override fun supportSubscribe(): Boolean {
        return true
    }

    override suspend fun subscribeFeed(title: String, feedId: String, categories: Array<String>): Boolean {
        val url = RssApi.id2url(feedId)
        val subscription = spiderServiceConnection.getFeed(url)
        subscription?.categories = ArrayList()
        if (categories != null) {
            for (i in categories.indices) {
                if (categories[i].isNullOrEmpty()) {
                    continue
                }
                val category = LocalRssCategory()
                category.id = categories[i]
                category.label = categories[i]
                subscription?.categories?.add(category)
            }
        }
        subscription?.lastClawTime = 0
        LocalFeedHelper.saveLocalFeed(subscription)
        return true
    }

    override suspend fun unsubscribeFeed(feedId: String): String {
        LocalFeedHelper.getLocalFeedMap().remove(feedId)
        val a: List<LocalRssSubscription> = LocalFeedHelper.getLocalFeedMap().values.toList()
        LocalFeedHelper.saveLocalFeed(a)
        return ""
    }

    override suspend fun editFeed(
        title: String,
        feedId: String,
        aCategories: Array<String>,
        rCategories: Array<String>
    ): String {
        return "ok"
    }

    override fun supportCreateTag(): Boolean {
        return false
    }

    override suspend fun getTags(): List<RssTag> {
        return ArrayList()
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
    }

    init {
        mainApi = MainApi()
    }
}