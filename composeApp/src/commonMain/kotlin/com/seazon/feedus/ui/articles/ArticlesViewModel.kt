package com.seazon.feedus.ui.articles

import androidx.lifecycle.viewModelScope
import com.seazon.feedus.cache.RssDatabase
import com.seazon.feedme.lib.rss.bo.Item
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.utils.jsonOf
import com.seazon.feedus.data.RssSDK
import com.seazon.feedus.data.TokenSettings
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArticlesViewModel(
    val rssSDK: RssSDK,
    val tokenSettings: TokenSettings,
    val rssDatabase: RssDatabase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(ArticlesScreenState())
    val state: StateFlow<ArticlesScreenState> = _state

    fun init(categoryId: String?, feedId: String?, starred: Boolean) {
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
                } else {
                    title = null
                }
                _state.update {
                    it.copy(
                        categoryId = categoryId,
                        feedId = feedId,
                        starred = starred,
                        title = title,
                        listType = listType,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace() // TODO error handing
                _state.update {
                    it.copy(
                        isLoading = false,
                        categoryId = categoryId,
                        feedId = feedId,
                        starred = starred,
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
                val rssStream = if (!state.value.categoryId.isNullOrEmpty()) {
                    if (Static.ACCOUNT_TYPE_FOLO == tokenSettings.getToken().accoutType) {
                        val feedIds =
                            rssDatabase.getFeeds()
                                .filter { it.categories?.contains(state.value.title.orEmpty()) ?: false }
                                .map { it.id }.toTypedArray()
                        api.getCategoryStream(jsonOf(feedIds), Static.FETCH_COUNT, null, state.value.continuation)
                    } else {
                        api.getCategoryStream(
                            state.value.categoryId.orEmpty(),
                            Static.FETCH_COUNT,
                            null,
                            state.value.continuation
                        )
                    }
                } else if (!state.value.feedId.isNullOrEmpty()) {
                    api.getFeedStream(state.value.feedId.orEmpty(), Static.FETCH_COUNT, null, state.value.continuation)
                } else if (state.value.starred) {
                    api.getStarredStreamIds(Static.FETCH_COUNT, state.value.continuation)
                } else {
                    api.getUnraedStream(Static.FETCH_COUNT, null, state.value.continuation)
                }
                val items = rssStream?.items?.map { convert(it) }.orEmpty()
                val feedMap = rssDatabase.getFeeds().associateBy { it.id }
                val hasMore = items.size >= Static.FETCH_COUNT
                _state.update {
                    it.copy(
                        isLoading = false,
                        hasMore = hasMore,
                        items = it.items + items,
                        feedMap = feedMap,
                        continuation = rssStream?.continuation,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace() // TODO error handing
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
            flag = Item.FLAG_UNREAD,
            status = 0,
            process = 0,
            star = 0,
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
        }
    }

    fun markAllRead() {
        if (state.value.items.isEmpty()) return

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val api = rssSDK.getRssApi(false)
            api.markRead(state.value.items.map { it.id }.toTypedArray())
            _state.update {
                it.copy(isLoading = false, items = emptyList())
            }
        }
    }

}