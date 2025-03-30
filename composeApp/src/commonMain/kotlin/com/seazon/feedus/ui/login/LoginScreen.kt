package com.seazon.feedus.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dokar.sonner.rememberToasterState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    navToFeeds: () -> Unit,
    navToExternalUrl: (url: String) -> Unit,
    navToDemo: () -> Unit,
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val toaster = rememberToasterState()
    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect {
            when (it) {
                is Event.OAuthSuccess -> navToFeeds()
                is Event.OAuthFailed -> toaster.show(it.message)
                else -> {}
            }
        }
    }
    LoginScreenComposable(
        setTokenAccountType = {
            viewModel.setTokenAccountTypeAndResetApi(it)
        },
        navToFeeds = navToFeeds,
        navToDemo = navToDemo,
        loginForLocalRss = {
            viewModel.loginForLocalRss(
                onSuccess = navToFeeds,
            )
        },
        startOAuth = { accountType ->
            viewModel.startOAuth(accountType) { url ->
                navToExternalUrl(url)
            }
        }
    )
}
