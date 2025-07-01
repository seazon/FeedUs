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
        stateFlow = vm.subscribeState,
        onDismiss = onDismiss,
        subscribe = { query ->
            vm.subscribe(
                query = query,
                onSuccess = onSuccess,
            )
        },
        onItemClick = {
            vm.subscribe2(
                title = it.title.orEmpty(),
                feedId = it.feedId.orEmpty(),
                feedUrl = it.feedUrl.orEmpty(),
                onSuccess = onSuccess,
            )
        }
    )
}
