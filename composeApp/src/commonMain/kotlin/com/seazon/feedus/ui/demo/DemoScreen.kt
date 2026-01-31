package com.seazon.feedus.ui.demo

import androidx.compose.runtime.Composable

@Composable
fun DemoScreen(
    navBack: () -> Unit,
    navToAI: () -> Unit,
) {
    DemoScreenComposable(
        navBack = navBack,
        navToAI = navToAI,
    )
}
