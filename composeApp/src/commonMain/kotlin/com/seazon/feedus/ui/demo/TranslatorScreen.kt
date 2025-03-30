package com.seazon.feedus.ui.demo

import androidx.compose.runtime.Composable
import com.seazon.feedus.getPlatform
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TranslatorScreen(
) {
    val viewModel = koinViewModel<TranslatorViewModel>()
    val platform = getPlatform()
    TranslatorScreenComposable(
        translate = { type, key, appId, query ->
            viewModel.translate(type, key, appId, query, platform.language)
        }
    )
}
