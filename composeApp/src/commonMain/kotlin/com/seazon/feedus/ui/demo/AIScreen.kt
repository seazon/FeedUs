package com.seazon.feedus.ui.demo

import androidx.compose.runtime.Composable
import com.seazon.feedus.getPlatform
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AIScreen() {
    val viewModel = koinViewModel<AIViewModel>()
    val platform = getPlatform()
    AIScreenComposable(
        stateFlow = viewModel.state,
        query = { type, key, model, query, prompt ->
            viewModel.query(type, key, model, query, platform.language, prompt)
        }
    )
}
