package com.seazon.feedus.ui.demo

import androidx.compose.runtime.Composable
import com.seazon.feedus.getPlatform
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SummaryScreen() {
    val viewModel = koinViewModel<SummaryViewModel>()
    val platform = getPlatform()
    SummaryScreenComposable(
        summary = { type, key, query ->
            viewModel.summary(type, key, query, platform.language)
        }
    )
}
