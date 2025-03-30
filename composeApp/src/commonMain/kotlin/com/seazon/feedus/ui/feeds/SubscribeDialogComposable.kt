package com.seazon.feedus.ui.feeds

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.seazon.feedus.ui.customize.FmOutlinedTextField
import feedus.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

@Composable
fun SubscribeDialogComposable(
    state: StateFlow<SubscribeDialogState>,
    onDismiss: () -> Unit = {},
    subscribe: (
        host: String?,
    ) -> Unit,
) {
    val state2 by state.collectAsState()
    var host by remember {
        mutableStateOf(state2.rssUrl)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(24.dp)
        ) {
            if (state2.errorTips.isNotEmpty()) {
                Text(
                    text = state2.errorTips,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            FmOutlinedTextField(
                text = host,
                placeHolder = stringResource(resource = Res.string.login_host),
                enabled = !state2.isLoading,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                onValueChange = {
                    host = it
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    modifier = Modifier.widthIn(max = 140.dp),
                    onClick = {
                        onDismiss()
                    }) {
                    Text(
                        text = stringResource(resource = Res.string.common_cancel).uppercase(),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (state2.isLoading) {
                    Spacer(modifier = Modifier.width(24.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
//                        progress = {
//                            0.8f
//                        }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                } else {
                    TextButton(modifier = Modifier.widthIn(max = 140.dp),
                        onClick = {
//                        errorTips.value = ""
                            keyboardController?.hide()
                            subscribe(host)
                        }) {
                        Text(
                            text = "subscribe".uppercase(),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SubscribeDialogComposablePreview() {
    val state = MutableStateFlow(SubscribeDialogState(isLoading = true, errorTips = "hello"))
    SubscribeDialogComposable(
        state = state,
        subscribe = { host ->

        }
    )
}