package com.seazon.feedus.ui.feeds

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
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
    val toaster = rememberToasterState()
    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect {
            when (it) {
                is Event.GeneralErrorEvent -> {
                    toaster.show(it.message)
                }

                else -> {}
            }
        }
    }
    Toaster(state = toaster)
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
        },
        onError = {
            toaster.show(it)
        }
    )
}
