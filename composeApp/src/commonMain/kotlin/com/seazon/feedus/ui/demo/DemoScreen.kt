package com.seazon.feedus.ui.demo

import androidx.compose.runtime.Composable

@Composable
fun DemoScreen(
    navBack: () -> Unit,
    navToTranslator: () -> Unit,
    navToSummary: () -> Unit,
) {
    DemoScreenComposable(
        navBack = navBack,
        navToTranslator = navToTranslator,
        navToSummary = navToSummary,
    )
}
