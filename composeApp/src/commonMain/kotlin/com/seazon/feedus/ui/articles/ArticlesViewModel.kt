package com.seazon.feedus.ui.articles

import androidx.lifecycle.viewModelScope
import com.seazon.feedus.cache.RssDatabase
import com.seazon.feedme.lib.rss.bo.Item
import com.seazon.feedme.lib.rss.bo.RssItem
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

    fun load(categoryId: String?, feedId: String?) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            var title: String? = null
            val api = rssSDK.getRssApi(false)
            try {
                val rssStream = if (!categoryId.isNullOrEmpty()) {
                    rssDatabase.getCategoryById(categoryId).let { category ->
                        title = category?.title
                    }
                    api.getCategoryStream(categoryId, 100, null, null)
                } else if (!feedId.isNullOrEmpty()) {
                    rssDatabase.getFeedById(feedId).let { feed ->
                        title = feed?.title
                    }
                    api.getFeedStream(feedId, 100, null, null)
                } else {
                    title = null
                    api.getUnraedStream(100, null, null)
                }
                val items = rssStream?.items?.map { convert(it) }.orEmpty()
                val feedMap = rssDatabase.getFeeds().associateBy { it.id }
                _state.update {
                    it.copy(isLoading = false, items = items, feedMap = feedMap, title = title)
                }
            } catch (e: Exception) {
                e.printStackTrace() // TODO error handing
                _state.update {
                    it.copy(isLoading = false, title = title)
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