package com.seazon.feedus.ui.login

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthDialog(
    type: String,
    tips: String = "",
    usernamePlaceholder: String? = null,
    guideLink: String? = null,
    onOpenGuideLink: (guideLink: String) -> Unit = {},
    onDismiss: () -> Unit = {},
    onSuccess: () -> Unit = {},
) {
    val vm: LoginViewModel = koinViewModel<LoginViewModel>()
    AuthDialogComposable(
        state = vm.state,
        type = type,
        tips = tips,
        usernamePlaceholder = usernamePlaceholder,
        guideLink = guideLink,
        onOpenGuideLink = onOpenGuideLink,
        onDismiss = onDismiss,
        login = { host, username, password, httpUsername, httpPassword ->
            vm.getUserInfo(
                host = host,
                username = username,
                password = password,
                httpUsername = httpUsername,
                httpPassword = httpPassword,
                onSuccess = onSuccess,
            )
        }
    )
}
