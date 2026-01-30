package com.seazon.feedus.ui.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedus.ui.customize.FmOutlinedTextField
import feedus.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthDialogComposable(
    state: StateFlow<LoginScreenState>,
    type: String,
    tips: String = "",
    usernamePlaceholder: String? = null,
    guideLink: String? = null,
    onOpenGuideLink: (guideLink: String) -> Unit = {},
    onDismiss: () -> Unit = {},
    login: (
        host: String?,
        username: String,
        password: String,
        httpUsername: String,
        httpPassword: String,
    ) -> Unit,
) {
    val state2 by state.collectAsState()
//    val errorTips = remember {
//        mutableStateOf(state2.value.errorTips)
//    }
    var host by remember {
        mutableStateOf(state2.host)
    }
    var username by remember {
        mutableStateOf(state2.username)
    }
    var password by remember {
        mutableStateOf(state2.password)
    }
    var httpUsername by remember {
        mutableStateOf("")
    }
    var httpPassword by remember {
        mutableStateOf("")
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tips,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.width(16.dp))
                if (!guideLink.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(resource = Res.drawable.ic_outline_help_center),
                        contentDescription = "help link",
                        modifier = Modifier
                            .size(32.dp, 32.dp)
                            .padding(8.dp)
                            .clickable {
                                onOpenGuideLink(guideLink.orEmpty())
                            },
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
            FmOutlinedTextField(
                text = username,
                placeHolder = usernamePlaceholder ?: stringResource(resource = Res.string.login_username),
                enabled = !state2.isLoading,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                onValueChange = {
                    username = it
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            FmOutlinedTextField(
                text = password,
                placeHolder = stringResource(resource = Res.string.login_password),
                enabled = !state2.isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go,
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
//                        errorTips.value = "hello error"
                        keyboardController?.hide()
                        login(host, username, password, httpUsername, httpPassword)
                    }
                ),
                onValueChange = {
                    password = it
                },
            )
            if (type == Static.ACCOUNT_TYPE_TTRSS) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(resource = Res.string.login_http_authentication))
                Spacer(modifier = Modifier.height(16.dp))
                FmOutlinedTextField(
                    text = httpUsername,
                    placeHolder = stringResource(resource = Res.string.login_http_username),
                    enabled = !state2.isLoading,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    onValueChange = {
                        httpUsername = it
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                FmOutlinedTextField(
                    text = httpPassword,
                    placeHolder = stringResource(resource = Res.string.login_http_password),
                    enabled = !state2.isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go,
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
//                            errorTips.value = ""
                            keyboardController?.hide()
                            login(host, username, password, httpUsername, httpPassword)
                        }
                    ),
                    onValueChange = {
                        httpPassword = it
                    },
                )
            }
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
                            login(host, username, password, httpUsername, httpPassword)
                        }) {
                        Text(
                            text = stringResource(resource = Res.string.login_login).uppercase(),
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
fun AuthDialogComposablePreview() {
    val stateFlow = MutableStateFlow(LoginScreenState(isLoading = true, errorTips = "hello"))
    AuthDialogComposable(
        state = stateFlow,
        type = Static.ACCOUNT_TYPE_TTRSS,
        tips = "Go to FreshRSS \\'profile\\' menu and open link behind \\'API password\\' to get host address",
        usernamePlaceholder = "user suername",
        guideLink = "http://baidu.com",
        login = { host, username, password, httpUsername, httpPassword ->

        }
    )
}