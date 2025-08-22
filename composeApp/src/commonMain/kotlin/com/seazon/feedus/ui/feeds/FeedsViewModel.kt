package com.seazon.feedus.ui.feeds

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.rss.bo.Category
import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.rss.service.explore.ExploreApi
import com.seazon.feedme.lib.rss.service.localrss.LocalRssApi
import com.seazon.feedme.lib.utils.HtmlUtils
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedus.cache.RssDatabase
import com.seazon.feedus.data.AppSettings
import com.seazon.feedus.data.RssSDK
import com.seazon.feedus.data.TokenSettings
import com.seazon.feedus.data.getFetchCnt
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Event {
    data class GeneralErrorEvent(val message: String) : Event()
}

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

    private val _eventFlow = MutableStateFlow<Event?>(null)
    val eventFlow: StateFlow<Event?> = _eventFlow

    init {
        if (isLogged()) {
            viewModelScope.launch {
                try {
                    val api = rssSDK.getRssApi(false)
                    val feeds =
                        rssDatabase.getFeeds()
                            .filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
                    val categories = rssDatabase.getCategories()
                        .filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
                    if (feeds.isEmpty()) {
                        sync()
                    } else {
                        val appPreferences = appSettings.getAppPreferences()
                        _state.update {
                            it.copy(
                                maxUnreadCount = appPreferences.unreadMax,
                                starredCount = appPreferences.starredCount,
                                feeds = feeds.sortedBy { it.title },
                                categories = categories.sortedBy { it.title },
                            )
                        }
                    }
                } catch (e: Exception) {
                    _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
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
            try {
                val api = rssSDK.getRssApi(false)

                fetchSubscription(api)
                fetchUnreadCount(api)

                val feeds =
                    rssDatabase.getFeeds().filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
                val categories =
                    rssDatabase.getCategories()
                        .filter { if (api.supportPagingFetchIds()) it.cntClientUnread > 0 else true }
                val appPreferences = appSettings.getAppPreferences()
                _state.update {
                    it.copy(
                        maxUnreadCount = appPreferences.unreadMax,
                        starredCount = appPreferences.starredCount,
                        feeds = feeds.sortedBy { it.title },
                        categories = categories.sortedBy { it.title },
                    )
                }
            } catch (e: Exception) {
                _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
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
            val stars = api.getStarredStreamIds(api.getFetchCnt(), null)
            // TODO this count just the FETCH_COUNT or less than FETCH_COUNT
            val starredCount = stars?.size.orZero()
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
                val unreadMax = if (unreadCounts?.max.orZero() == 0) max else unreadCounts?.max.orZero()
                rssDatabase.saveCategories(categoryMap.values.toList())
                rssDatabase.saveFeeds(feedMap.values.toList())
                appSettings.getAppPreferences().apply {
                    appSettings.saveAppPreferences(
                        appPreferences = this.copy(
                            unreadMax = unreadMax,
                            starredCount = starredCount,
                        )
                    )
                }
            } else {
                // TODO for the rss services which not support supportPagingFetchIds(), need a way to show feeds
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun subscribe(
        query: String?,
        onSuccess: () -> Unit,
    ) {
        if (query.isNullOrEmpty()) return

        viewModelScope.launch {
            try {
                val api = rssSDK.getRssApi(false)
                when (api.getToken()?.accoutType) {
                    Static.ACCOUNT_TYPE_LOCAL_RSS -> {
                        (api as LocalRssApi).search(query)?.let {
                            subscribe2(it.title.orEmpty(), it.id.orEmpty(), query, onSuccess)
                        } ?: run {
                            _subscribeState.update {
                                it.copy(
                                    errorTips = "Not found",
                                )
                            }
                        }
                    }

                    else -> {
                        if (HtmlUtils.isHttpUrl(query)) {
                            subscribe2(query, "feed/${query}", query, onSuccess)
                        } else {
                            val results = ExploreApi.search(query, api)
                            if (results.isEmpty()) {
                                _subscribeState.update {
                                    it.copy(
                                        errorTips = "Not found",
                                    )
                                }
                            } else {
                                _subscribeState.update {
                                    it.copy(
                                        results = results,
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
            }
        }
    }

    fun subscribe2(
        title: String,
        feedId: String,
        feedUrl: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val api = rssSDK.getRssApi(false)
                val subscribeFeedUrl = if (api.getToken()?.accoutType == Static.ACCOUNT_TYPE_FOLO) feedUrl else feedId
                val success = api.subscribeFeed(title, subscribeFeedUrl, arrayOf())
                if (success) {
                    onSuccess()
                } else {
                    _subscribeState.update {
                        it.copy(
                            errorTips = "Subscribe failed",
                        )
                    }
                }
            } catch (e: Exception) {
                _subscribeState.update {
                    it.copy(
                        errorTips = "Subscribe failed: ${e.message.orEmpty()}",
                    )
                }
            }
        }
    }
}