package com.seazon.feedus.ui.demo

import androidx.compose.runtime.Composable

@Composable
fun DemoScreen(
    navToTranslator: () -> Unit,
) {
    DemoScreenComposable(
        navToTranslator = navToTranslator,
    )
}
