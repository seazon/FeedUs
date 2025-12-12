package com.seazon.feedus.ui.articles

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.rss.bo.Item
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.utils.jsonOf
import com.seazon.feedus.cache.RssDatabase
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

class ArticlesViewModel(
    val rssSDK: RssSDK,
    val tokenSettings: TokenSettings,
    val rssDatabase: RssDatabase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(ArticlesScreenState())
    val state: StateFlow<ArticlesScreenState> = _state

    private val _eventFlow = MutableStateFlow<Event?>(null)
    val eventFlow: StateFlow<Event?> = _eventFlow

    fun init(categoryId: String?, feedId: String?, starred: Boolean, labelId: String?) {
        viewModelScope.launch {
            try {
                // render title bar
                var title: String? = null
                var listType = ListType.NORMAL
                if (!categoryId.isNullOrEmpty()) {
                    rssDatabase.getCategoryById(categoryId).let { category ->
                        title = category?.title
                    }
                } else if (!feedId.isNullOrEmpty()) {
                    rssDatabase.getFeedById(feedId).let { feed ->
                        title = feed?.title
                    }
                } else if (starred) {
                    title = null
                    listType = ListType.STARRED
                } else if (!labelId.isNullOrEmpty()) {
                    rssDatabase.getLabelById(labelId).let { label ->
                        title = label?.label
                    }
                    listType = ListType.TAG
                } else {
                    title = null
                }
                _state.update {
                    it.copy(
                        categoryId = categoryId,
                        feedId = feedId,
                        starred = starred,
                        labelId = labelId,
                        title = title,
                        listType = listType,
                    )
                }
            } catch (e: Exception) {
                _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
                _state.update {
                    it.copy(
                        isLoading = false,
                        categoryId = categoryId,
                        feedId = feedId,
                        starred = starred,
                        labelId = labelId,
                    )
                }
            }
        }
    }

    fun load() {
        _state.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch {
            try {
                // fetch item and render list
                val api = rssSDK.getRssApi(false)
                var rssStream = if (!state.value.categoryId.isNullOrEmpty()) {
                    if (Static.ACCOUNT_TYPE_FOLO == tokenSettings.getToken().accoutType) {
                        val feedIds = rssDatabase.getFeeds()
                            .filter { it.categories?.contains(state.value.title.orEmpty()) ?: false }
                            .map { it.id }.toTypedArray()
                        api.getCategoryStream(jsonOf(feedIds), api.getFetchCnt(), null, state.value.continuation)
                    } else {
                        api.getCategoryStream(
                            state.value.categoryId.orEmpty(),
                            api.getFetchCnt(),
                            null,
                            state.value.continuation
                        )
                    }
                } else if (!state.value.feedId.isNullOrEmpty()) {
                    api.getFeedStream(state.value.feedId.orEmpty(), api.getFetchCnt(), null, state.value.continuation)
                } else if (state.value.starred) {
                    if (api.supportStarV2()) {
                        api.getStarredStream(api.getFetchCnt(), state.value.continuation)
                    } else {
                        api.getStarredStreamIds(api.getFetchCnt(), state.value.continuation)
                    }
                } else if (!state.value.labelId.isNullOrEmpty()) {
                    rssDatabase.getLabelById(state.value.labelId.orEmpty())?.let { label ->
                        api.getTagStream(label.label.orEmpty(), api.getFetchCnt(), state.value.continuation)
                    }
                } else {
                    api.getUnraedStream(api.getFetchCnt(), null, state.value.continuation)
                }
                if (rssStream?.items.isNullOrEmpty() && !rssStream?.ids.isNullOrEmpty()) {
                    rssStream = api.getStreamByIds(rssStream.ids.take(api.getFetchCnt()).toTypedArray())
                }
                val items = rssStream?.items?.map { convert(it) }.orEmpty()
                val feedMap = rssDatabase.getFeeds().associateBy { it.id }
                val hasMore = items.size >= api.getFetchCnt()
                _state.update {
                    it.copy(
                        isLoading = false,
                        hasMore = api.supportPagingFetchIds() && hasMore,
                        items = it.items + items,
                        feedMap = feedMap,
                        continuation = rssStream?.continuation,
                    )
                }
            } catch (e: Exception) {
                _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private fun convert(it: RssItem): Item {
        return Item(
            id = it.id.orEmpty(),
            fid = it.fid,
            flag = if (it.isUnread) Item.FLAG_UNREAD else Item.FLAG_READ,
            status = 0,
            process = 0,
            star = if (it.isStar == true) Item.STAR_STARRED else Item.STAR_UNSTAR,
            tag = 0,
            title = it.title,
            titleTranslated = null,
            link = it.link,
            visual = it.visual,
            author = it.author,
            publishedDate = it.publisheddate,
            updatedDate = it.updateddate,
            description = it.description,
            tags = it.tags,
        )
    }

    fun markRead(item: Item) {
        if (item.flag == Item.FLAG_READ) return

        viewModelScope.launch {
            try {
                val api = rssSDK.getRssApi(false)
                api.markRead(arrayOf(item.id))
                item.flag = Item.FLAG_READ
                rssDatabase.updateItemFlag(item)
                rssDatabase.getFeedById(item.fid.orEmpty())?.let { feed ->
                    feed.cntClientUnread -= 1
                    rssDatabase.updateFeedCntClientUnread(feed)
                }
                _state.update {
                    it.copy(items = it.items)
                }
            } catch (e: Exception) {
                _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
            }
        }
    }

    fun markAllRead() {
        if (state.value.items.isEmpty()) return

        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(isLoading = true)
                }
                val api = rssSDK.getRssApi(false)
                api.markRead(state.value.items.map { it.id }.toTypedArray())
                _state.update {
                    it.copy(isLoading = false, items = emptyList())
                }
            } catch (e: Exception) {
                _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
                _state.update {
                    it.copy(isLoading = false)
                }
            }

        }
    }

    fun toggleStar(item: Item) {
        viewModelScope.launch {
            try {
                val api = rssSDK.getRssApi(false)
                val newItem = if (item.star == Item.STAR_STARRED) {
                    api.markUnstar(arrayOf(item.id))
                    item.copy(star = Item.STAR_UNSTAR)
                } else {
                    api.markStar(arrayOf(item.id))
                    item.copy(star = Item.STAR_STARRED)
                }
                rssDatabase.updateItemStar(newItem)

                _state.update {
                    val newItems = it.items.map { i ->
                        if (i.id == item.id) newItem else i
                    }
                    it.copy(items = newItems)
                }
            } catch (e: Exception) {
                _eventFlow.value = Event.GeneralErrorEvent(e.message.orEmpty())
            }
        }
    }
}