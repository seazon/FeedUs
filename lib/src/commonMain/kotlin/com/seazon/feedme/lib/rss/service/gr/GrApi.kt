package com.seazon.feedme.lib.rss.service.gr

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.SelfHostedRssApi
import com.seazon.feedme.lib.rss.service.gr.api.AuthenticationApi
import com.seazon.feedme.lib.rss.service.gr.api.MainApi
import com.seazon.feedme.lib.rss.service.gr.bo.convert
import com.seazon.feedme.lib.rss.service.gr.bo.convert2
import com.seazon.feedme.lib.utils.orZero

abstract class GrApi(token: RssToken, schemaHttps: String?, expiredTimestamp: Long) : RssApi,
    SelfHostedRssApi {
    protected var config: GrConfig
    protected var _token: RssToken? = null
    protected var authenticationApi: AuthenticationApi
    protected var mainApi: MainApi? = null

    init {
        config = GrConfig(schemaHttps, expiredTimestamp)
        authenticationApi = AuthenticationApi(token, config)
    }

    override fun setToken(token: RssToken) {
        this._token = token
        mainApi = MainApi(token, config, this)
    }

    override fun getToken(): RssToken? {
        return _token
    }

    override fun getCategoryId(category: String): String? {
        return "user/" + _token?.id + "/label/" + category
    }

    /*
     * AuthenticationApi
     */
    override fun getOAuth2Url(state: String): String? {
        return if (getAuthType() == RssApi.AUTH_TYPE_OAUTH2) {
            authenticationApi.getOAuth2Url(state)
        } else {
            null
        }
    }

    override suspend fun getRefreshToken(code: String): String? {
        return if (getAuthType() == RssApi.AUTH_TYPE_OAUTH2) {
            authenticationApi.getRefreshToken(code)
        } else {
            null
        }
    }

    override suspend fun getAccessToken(token: RssToken): String? {
        return if (getAuthType() == RssApi.AUTH_TYPE_OAUTH2) {
            authenticationApi.getAccessTokenOAuth2(token.refreshToken)
        } else if (getAuthType() == RssApi.AUTH_TYPE_BASE) {
            authenticationApi.getAccessToken(token.username, token.password)
        } else {
            null
        }
    }

    override suspend fun setUserWithAccessToken(token: RssToken, response: String) {
        if (getAuthType() == RssApi.AUTH_TYPE_OAUTH2) {
            authenticationApi.setUserWithAccessTokenOAuth2(token, response)
        } else if (getAuthType() == RssApi.AUTH_TYPE_BASE) {
            authenticationApi.setUserWithAccessToken(token, response)
        } else {
        }
    }

    override suspend fun setUserWithRefreshToken(token: RssToken, response: String) {
        if (getAuthType() == RssApi.AUTH_TYPE_OAUTH2) {
            authenticationApi.setUserWithRefreshToken(token, response)
        } else {
        }
    }

    override suspend fun setUserInfo(token: RssToken) {
        val response = mainApi?.getUserInfo()
        token.id = response?.userId
        token.email = response?.userEmail
    }

    /*
     * EntriesApi
     */
    /*
     * MarkersApi
     */
    override suspend fun getUnreadCounts(): RssUnreadCounts? {
        return mainApi?.getUnreadCounts()?.convert()
    }

    override suspend fun markRead(entryIds: Array<String>?): String? {
        return mainApi?.editTag(GrConstants.TAG_ACTION_ADD, GrConstants.TAG_READ, entryIds)
    }

    override suspend fun markUnread(entryIds: Array<String>?): String? {
        return mainApi?.editTag(GrConstants.TAG_ACTION_REMOVE, GrConstants.TAG_READ, entryIds)
    }

    override suspend fun markStar(entryIds: Array<String>): String? {
        return mainApi?.editTag(GrConstants.TAG_ACTION_ADD, GrConstants.TAG_STARRED, entryIds)
    }

    override suspend fun markUnstar(entryIds: Array<String>): String? {
        return mainApi?.editTag(GrConstants.TAG_ACTION_REMOVE, GrConstants.TAG_STARRED, entryIds)
    }

    override suspend fun markTag(entryIds: Array<String>, tagIds: Array<String>) {
        for (i in tagIds.indices) {
            if (tagIds[i].isNullOrEmpty()) {
                continue
            }
            mainApi?.editTag(GrConstants.TAG_ACTION_ADD, "user/-/label/" + tagIds[i], entryIds)
        }
    }

    override suspend fun markUntag(entryIds: Array<String>, tagIds: Array<String>) {
        for (i in tagIds.indices) {
            if (tagIds[i].isNullOrEmpty()) {
                continue
            }
            mainApi?.editTag(GrConstants.TAG_ACTION_REMOVE, "user/-/label/" + tagIds[i], entryIds)
        }
    }

    /*
     * StreamsApi
     */
    override suspend fun getStreamByIds(entryIds: Array<String>): RssStream? {
        return mainApi?.getContentsByIds(entryIds)?.convert()
    }

    override suspend fun getUnraedStreamIds(count: Int, continuation: String?): RssStream? {
        return mainApi?.getContentsIds(
            "user/-/state/com.google/reading-list",
            count,
            true,
            continuation
        )?.convert()
    }

    override suspend fun getFeedStreamIds(
        feedId: String,
        count: Int,
        continuation: String?
    ): RssStream? {
        return mainApi?.getContentsIds(feedId, count, true, continuation)?.convert()
    }

    override suspend fun getCategoryStreamIds(
        category: String,
        count: Int,
        continuation: String?
    ): RssStream? {
        return mainApi?.getContentsIds(category, count, true, continuation)?.convert()
    }

    override suspend fun getUnraedStream(
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getContents(
            "user/-/state/com.google/reading-list",
            count,
            true,
            since,
            continuation
        )?.convert()
    }

    override suspend fun getFeedStream(
        feedId: String,
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getContents(feedId, count, true, since, continuation)?.convert()
    }

    override suspend fun getCategoryStream(
        category: String,
        count: Int,
        since: String?,
        continuation: String?
    ): RssStream? {
        return mainApi?.getContents(category, count, true, since, continuation)?.convert()
    }

    override suspend fun getTagStreamIds(
        tag: String,
        count: Int,
        continuation: String?
    ): RssStream? {
        return mainApi?.getContentsIds("user/-/label/$tag", count, false, continuation)?.convert()
    }

    override suspend fun getStarredStreamIds(count: Int, continuation: String?): RssStream? {
        return mainApi?.getContentsIds(tagStarred, count, false, continuation)?.convert()
    }

    override fun supportStar(): Boolean {
        return true
    }

    /*
     * SubscriptionsApi
     */
    override suspend fun getSubscriptions(): List<RssFeed>? {
        return mainApi?.getSubscriptions()?.subscriptions?.convert2()
    }

    override fun supportSubscribe() = true

    override fun supportUpdateSubscription() = false

    override suspend fun subscribeFeed(
        title: String,
        feedId: String,
        categories: Array<String>
    ): Boolean {
//        String feedIdPrefix = "feed/";
//        String feedUrl = feedId.substring(feedIdPrefix.length());
//        GrQuickAdd quickAdd = GrQuickAdd.parse(mainApi.quickadd(feedUrl));
//        if (quickAdd.getError() != null) {
//            return quickAdd.getError();
//        }
//          mainApi.editSubscription("subscribe", feedId, title, categories, null);
        mainApi?.editSubscription("subscribe", feedId, title, categories, null)
        return true // TODO should check response for error case
    }

    override suspend fun unsubscribeFeed(feedId: String): String? {
        return mainApi?.editSubscription("unsubscribe", feedId, null, null, null)
    }

    override suspend fun editFeed(
        title: String,
        feedId: String,
        aCategories: Array<String>,
        rCategories: Array<String>
    ): String? {
        return mainApi?.editSubscription("edit", feedId, title, aCategories, rCategories)
    }

    /*
     * TagsApi
     */
    override fun supportCreateTag(): Boolean {
        return true
    }

    override suspend fun getTags(): List<RssTag>? {
        return mainApi?.getTags()?.tags?.mapNotNull {
            if (it.id.isNullOrEmpty()) {
                null
            } else {
                RssTag(it.id, it.id?.substring(it.id?.lastIndexOf("/").orZero() + 1))
            }
        }?.filter {
            !GrConstants.isIgnoredTag(it.label.orEmpty()) && !GrConstants.isIgnoredForTag(it.label.orEmpty())
        }
    }

    override suspend fun deleteTags(tagIds: Array<String>) {
    }

    override fun supportPagingFetchIds(): Boolean {
        return true
    }

    override fun supportFetchByFeed(): Boolean {
        return true
    }

    private val tagStarred: String
        private get() = GrConstants.TAG_STARRED

    override fun getDefaultHost(): String? {
        return config.schemaHttps
    }

    companion object {
        fun isLabel(s: String): Boolean {
            return try {
                val i = s.indexOf("/", 5)
                val e = s.indexOf("/", i + 1)
                if ("label" == s.substring(i + 1, e)) {
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }

        fun getLabel(s: String): String {
            return s.substring(s.lastIndexOf("/") + 1)
        }
    }
}
