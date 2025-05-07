package com.seazon.feedme.lib.rss.service.ttrss

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.RssApi.Companion.id2url
import com.seazon.feedme.lib.rss.service.SelfHostedRssApi
import com.seazon.feedme.lib.rss.service.convert
import com.seazon.feedme.lib.rss.service.ttrss.api.AuthenticationApi
import com.seazon.feedme.lib.rss.service.ttrss.api.MainApi
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssCategory
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssCounterList
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssStream
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssSubscription

class TtrssApi(token: RssToken) : RssApi, SelfHostedRssApi {
    private var token: RssToken? = null
    protected var authenticationApi: AuthenticationApi
    protected var mainApi: MainApi? = null

    init {
        authenticationApi = AuthenticationApi(token)
    }

    override fun getToken(): RssToken? {
        return token
    }

    override fun setToken(token: RssToken) {
        this.token = token
        mainApi = MainApi(token)
    }

    override fun getCategoryId(category: String): String? {
        return null
    }

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

    override fun setUserWithRefreshToken(token: RssToken, response: String) {
    }

    override suspend fun getAccessToken(token: RssToken): String {
        return authenticationApi.getAccessToken(token.username.orEmpty(), token.password.orEmpty(), token.httpUsername, token.httpPassword)
    }

    override fun setUserWithAccessToken(token: RssToken, response: String) {
        authenticationApi.setUserWithAccessToken(token, response)
    }

    override suspend fun setUserInfo(token: RssToken) {
        token.email = token.username
    }

    override fun supportPagingFetchIds(): Boolean {
        return true
    }

    override fun supportFetchByFeed(): Boolean {
        return true
    }

    override suspend fun getUnreadCounts(): RssUnreadCounts {
        return TtrssCounterList.parse(mainApi?.getCounters())
    }

    override suspend fun markRead(entryIds: Array<String>?): String? {
        return mainApi?.updateArticle(entryIds, 0, 2)
    }

    override suspend fun markUnread(entryIds: Array<String>?): String? {
        return mainApi?.updateArticle(entryIds, 1, 2)
    }

    override suspend fun markStar(entryIds: Array<String>): String? {
        return mainApi?.updateArticle(entryIds, 1, 0)
    }

    override suspend fun markUnstar(entryIds: Array<String>): String? {
        return mainApi?.updateArticle(entryIds, 0, 0)
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
        val tagMap: MutableMap<String?, String?> = HashMap()
        for (tag in getTags()) {
            tagMap[tag.label] = tag.id
        }
        for (i in tagIds.indices) {
            if (tagIds[i].isEmpty()) {
                continue
            }
            mainApi?.setArticleLabel(true, tagMap[tagIds[i]].orEmpty(), entryIds)
        }
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
        val tagMap: MutableMap<String?, String?> = HashMap()
        for (tag in getTags()) {
            tagMap[tag.label] = tag.id
        }
        for (i in tagIds.indices) {
            if (tagIds[i].isEmpty()) {
                continue
            }
            mainApi?.setArticleLabel(false, tagMap[tagIds[i]].orEmpty(), entryIds)
        }
    }

    override suspend fun getStreamByIds(entryIds: Array<String>): RssStream? {
        return mainApi?.getArticle(entryIds)?.convert()
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream {
        return TtrssStream.parseIds(
            mainApi?.getHeadlines(
                "-4", false, false,
                true, count, "0", continuation
            ), continuation
        )
    }

    override suspend fun getFeedStreamIds(feedId: String, count: Int, continuation: String?): RssStream {
        return TtrssStream.parseIds(
            mainApi?.getHeadlines(
                unwrapFeedId(feedId), false, false,
                true, count, "0", continuation
            ), continuation
        )
    }

    override suspend fun getCategoryStreamIds(category: String, count: Int, continuation: String?): RssStream {
        return TtrssStream.parseIds(
            mainApi?.getHeadlines(
                unwrapCategoryId(category), true, false,
                true, count, "0", continuation
            ), continuation
        )
    }

    override suspend fun getUnraedStream(count: Int, since: String?, continuation: String?): RssStream {
        return TtrssStream.parseIds(
            mainApi?.getHeadlines(
                "-4", false, true,
                true, count, since, continuation
            ), continuation
        )
    }

    override suspend fun getFeedStream(feedId: String, count: Int, since: String?, continuation: String?): RssStream {
        return TtrssStream.parseIds(
            mainApi?.getHeadlines(
                unwrapFeedId(feedId), false, true,
                true, count, since, continuation
            ), continuation
        )
    }

    override suspend fun getCategoryStream(category: String, count: Int, since: String?, continuation: String?): RssStream {
        return TtrssStream.parseIds(
            mainApi?.getHeadlines(
                unwrapCategoryId(category), true, true,
                true, count, since, continuation
            ), continuation
        )
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream {
        return TtrssStream.parseIds(
            mainApi?.getHeadlines(
                "-1", false, false,
                false, count, "0", continuation
            ), continuation
        )
    }

    override fun supportStar(): Boolean {
        return true
    }

    override suspend fun getSubscriptions(): List<RssFeed> {
        val feeds = mainApi?.getFeeds()
        return TtrssSubscription.parse(feeds, TtrssCategory.parse(mainApi?.getCategories().orEmpty(), true))
    }

    override fun supportSubscribe(): Boolean {
        return true
    }

    override suspend fun subscribeFeed(title: String, feedId: String, categories: Array<String>): Boolean {
        // 需要订阅到其他类别，但是由于ttrss api不支持添加类别，所以暂时默认添加到未分类的，或者已有分类
        val url = id2url(feedId)
        val map = TtrssCategory.parse(mainApi?.getCategories().orEmpty(), false)
        for (i in categories.indices) {
            if (categories[i].isEmpty()) {
                continue
            }
            val category = map[categories[i]]
            if (category != null) {
                mainApi?.subscribeToFeed(url, category.id.orEmpty())
            } else {
                mainApi?.subscribeToFeed(url, "0")
            }
        }
        return true
    }

    override suspend fun unsubscribeFeed(id: String): String? {
        return mainApi?.unsubscribeFeed(unwrapFeedId(id))
    }

    override fun supportCreateTag(): Boolean {
        return false
    }

    override suspend fun getTags(): List<RssTag> {
        return mainApi?.getLabels()?.content?.map {
            RssTag(
                it.id,
                it.caption
            )
        }.orEmpty()
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
    }

    companion object {
        @JvmStatic
        fun wrapFeedId(id: String): String {
            return TtrssSubscription.ID_PREFIX + id
        }

        fun unwrapFeedId(id: String): String {
            return if (id.startsWith(TtrssSubscription.ID_PREFIX)) {
                id.substring(TtrssSubscription.ID_PREFIX.length)
            } else {
                id
            }
        }

        @JvmStatic
        fun wrapCategoryId(id: String): String {
            return TtrssCategory.ID_PREFIX + id
        }

        fun unwrapCategoryId(id: String): String {
            return if (id.startsWith(TtrssCategory.ID_PREFIX)) {
                id.substring(TtrssCategory.ID_PREFIX.length)
            } else {
                id
            }
        }
    }
}
