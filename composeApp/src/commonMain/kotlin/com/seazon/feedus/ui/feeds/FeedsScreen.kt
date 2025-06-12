package com.seazon.feedus.ui.feeds

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedsScreen(
    navToArticles: (categoryId: String?, feedId: String?, starred: Boolean) -> Unit,
    navToLogin: () -> Unit,
    navToDemo: () -> Unit,
) {
    val viewModel = koinViewModel<FeedsViewModel>()
    if (!viewModel.isLogged()) {
        navToLogin()
        return
    }
    FeedsScreenComposable(
        stateFlow = viewModel.state,
        navToArticles = navToArticles,
        navToDemo = navToDemo,
        sync = {
            viewModel.sync()
        },
        logout = {
            viewModel.logout {
                navToLogin()
            }
        })
}
