package com.seazon.feedus.ui.demo

import androidx.compose.runtime.Composable

@Composable
fun DemoScreen(
    navToTranslator: () -> Unit,
    navToSummary: () -> Unit,
) {
    DemoScreenComposable(
        navToTranslator = navToTranslator,
        navToSummary = navToSummary,
    )
}
