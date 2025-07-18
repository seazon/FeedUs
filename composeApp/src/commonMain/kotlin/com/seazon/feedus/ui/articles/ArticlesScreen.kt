package com.seazon.feedus.ui.articles

import androidx.compose.runtime.Composable
import com.seazon.feedme.lib.rss.bo.Item
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ArticlesScreen(
    categoryId: String?,
    feedId: String?,
    starred: Boolean,
    navBack: () -> Unit,
    navToArticle: (item: Item) -> Unit,
) {
    val viewModel = koinViewModel<ArticlesViewModel>()
    viewModel.init(categoryId, feedId, starred)
    ArticlesScreenComposable(
        stateFlow = viewModel.state,
        onItemClick = {
            viewModel.markRead(it)
            navToArticle(it)
        },
        onToggleStar = {
            viewModel.toggleStar(it)
        },
        navBack = navBack,
        markAllRead = {
            viewModel.markAllRead()
        },
        loadMoreData = {
            viewModel.load()
        }
    )
}