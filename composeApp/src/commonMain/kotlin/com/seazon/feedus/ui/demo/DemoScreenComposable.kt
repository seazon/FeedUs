package com.seazon.feedus.ui.demo

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seazon.feedus.ui.customize.noRippleClickable
import feedus.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoScreenComposable(
    navBack: () -> Unit,
    navToTranslator: () -> Unit,
    navToSummary: () -> Unit,
) {
    MainContent(
        navBack = navBack,
        navToTranslator = navToTranslator,
        navToSummary = navToSummary,
    )
}

@Composable
private fun MainContent(
    navBack: () -> Unit,
    navToTranslator: () -> Unit,
    navToSummary: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        horizontalAlignment = Alignment.Start,
    ) {
        AppBar(navBack)
        Text(
            text = stringResource(Res.string.translator_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(16.dp).noRippleClickable {
                navToTranslator()
            }
        )
        Text(
            text = stringResource(Res.string.summary_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(16.dp).noRippleClickable {
                navToSummary()
            }
        )
    }
}

@Composable
fun AppBar(navBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    navBack()
                },
                modifier = Modifier.padding(start = 2.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.demo_title),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        elevation = 0.dp,
        actions = {
        }
    )
}

@Preview
@Composable
fun DemoScreenComposablePreview() {
    DemoScreenComposable(
        navBack = {},
        navToTranslator = {},
        navToSummary = {},
    )
}
