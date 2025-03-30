package com.seazon.feedus.ui.feeds

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SubscribeDialog(
    onDismiss: () -> Unit = {},
    onSuccess: () -> Unit = {},
) {
    val vm: FeedsViewModel = koinViewModel<FeedsViewModel>()
    SubscribeDialogComposable(
        state = vm.subscribeState,
        onDismiss = onDismiss,
        subscribe = { host ->
            vm.subscribe(
                host = host,
                onSuccess = onSuccess,
            )
        }
    )
}
