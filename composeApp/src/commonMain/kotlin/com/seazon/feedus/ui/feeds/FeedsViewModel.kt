package com.seazon.feedus.ui.feeds

import androidx.lifecycle.viewModelScope
import com.seazon.feedus.cache.RssDatabase
import com.seazon.feedme.lib.rss.bo.Category
import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.rss.service.localrss.LocalRssApi
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedus.data.AppSettings
import com.seazon.feedus.data.RssSDK
import com.seazon.feedus.data.TokenSettings
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedsViewModel(
    val rssSDK: RssSDK,
    val tokenSettings: TokenSettings,
    val rssDatabase: RssDatabase,
    val appSettings: AppSettings,
) : BaseViewModel() {

    private val _state = MutableStateFlow(
        FeedsScreenState(
            serviceName = rssSDK.tokenSettings.getToken().getAccountTypeName().orEmpty()
        )
    )
    val state: StateFlow<FeedsScreenState> = _state

    private val _subscribeState = MutableStateFlow(SubscribeDialogState())
    val subscribeState: StateFlow<SubscribeDialogState> = _subscribeState

    init {
        if (isLogged()) {
            viewModelScope.launch {
                val api = rssSDK.getRssApi(false)
                val feeds =
                    rssDatabase.getFeeds().filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
                val categories = rssDatabase.getCategories()
                    .filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
                if (feeds.isEmpty()) {
                    sync()
                } else {
                    val appPreferences = appSettings.getAppPreferences()
                    _state.update {
                        it.copy(
                            maxUnreadCount = appPreferences.unreadMax,
                            feeds = feeds,
                            categories = categories,
                        )
                    }
                }
            }
        }
    }

    fun isLogged(): Boolean {
        return tokenSettings.getToken().isAuthed()
    }

    fun logout(callback: () -> Unit) {
        viewModelScope.launch {
            tokenSettings.clear()
            appSettings.clear()
            rssDatabase.clearCategories()
            rssDatabase.clearItems()
            rssDatabase.clearFeeds()

            callback()
        }
    }

    fun sync() {
        if (!isLogged()) {
            return
        }
        viewModelScope.launch {
            val api = rssSDK.getRssApi(false)

            fetchSubscription(api)
            fetchUnreadCount(api)

            val feeds =
                rssDatabase.getFeeds().filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
            val categories =
                rssDatabase.getCategories().filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
            val appPreferences = appSettings.getAppPreferences()
            _state.update {
                it.copy(
                    maxUnreadCount = appPreferences.unreadMax, feeds = feeds, categories = categories
                )
            }
        }
    }

    private suspend fun fetchSubscription(api: RssApi) {
        val subscriptions = api.getSubscriptions()
        val tagMap = mutableMapOf<String, RssTag>()
        val feeds = subscriptions?.map { feed ->
            feed.categories?.forEach { tag ->
                if (!tag.id.isNullOrEmpty()) {
                    tagMap[tag.id.orEmpty()] = tag
                }
            }
            Feed(
                id = feed.id.orEmpty(),
                title = feed.title,
                sortId = null,
                url = feed.url,
                feedUrl = feed.feedUrl,
                categories = feed.categories?.joinToString(",", transform = { it.label.orEmpty() }),
                favicon = feed.favicon,
                cntClientAll = 0,
                cntClientUnread = 0,
            )
        }
        rssDatabase.saveFeeds(feeds.orEmpty())

        val categories = tagMap.values.map {
            Category(
                id = it.id.orEmpty(),
                title = it.label.orEmpty(),
                sortId = null,
                cntClientAll = 0,
                cntClientUnread = 0,
            )
        }
        rssDatabase.saveCategories(categories)
    }

    private suspend fun fetchUnreadCount(api: RssApi) {
        try {
            // TODO use in app
            val tags = api.getTags()
            val stars = api.getStarredStreamIds(Static.FETCH_COUNT, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (api.supportPagingFetchIds()) {
            val unreadCounts = api.getUnreadCounts()
            val categoryMap = rssDatabase.getCategories().apply {
                this.forEach {
                    it.cntClientUnread = 0
                }
            }.associateBy { it.id }
            val feedMap = rssDatabase.getFeeds().associateBy { it.id }
            var max = 0
            unreadCounts?.unreadCounts?.forEach {
                feedMap[it.id]?.let { feed ->
                    feed.cntClientUnread = it.count
                    max += it.count
                    // for folo or similar case which unread counts api won't provide count for category
                    categoryMap[feed.categories]?.cntClientUnread += it.count
                } ?: run {
                    categoryMap[it.id]?.cntClientUnread = it.count
                }
            }
            rssDatabase.saveCategories(categoryMap.values.toList())
            rssDatabase.saveFeeds(feedMap.values.toList())
            appSettings.getAppPreferences().apply {
                appSettings.saveAppPreferences(this.copy(unreadMax = if (unreadCounts?.max.orZero() == 0) max else unreadCounts?.max.orZero()))
            }
        } else {
            // TODO for the rss services which not support supportPagingFetchIds(), need a way to show feeds
        }
    }

    fun subscribe(
        host: String?,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            val api = rssSDK.getRssApi(false)
            when (api) {
                is LocalRssApi -> {
                    api.search(host)?.let {
                        if (api.subscribeFeed(it.title.orEmpty(), it.id.orEmpty(), emptyArray<String>())) {
                            onSuccess()
                        } else {
                            _subscribeState.update {
                                it.copy(
                                    errorTips = "Subscribe failed",
                                )
                            }
                        }

                    } ?: run {
                        _subscribeState.update {
                            it.copy(
                                errorTips = "Not found",
                            )
                        }
                    }
                }

                else -> {
                    // TODO handle other cases
                }
            }
        }
    }
}