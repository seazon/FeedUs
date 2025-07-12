package com.seazon.feedus.ui.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedus.ui.customize.noRippleClickable
import feedus.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreenComposable(
    setTokenAccountType: (type: String) -> Unit,
    navToFeeds: () -> Unit,
    navToDemo: () -> Unit,
    loginForLocalRss: () -> Unit,
    startOAuth: (accountType: String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    var openDialog by remember {
        mutableStateOf(false)
    }
    var type by remember {
        mutableStateOf("")
    }
    MainContent(
        onItemClick = {
            onItemClick(
                type = it.type,
                onShowDialog = {
                    setTokenAccountType(it.type)
                    openDialog = true
                    type = it.type
                },
                loginForLocalRss = loginForLocalRss,
                startOAuth = startOAuth,
            )
        },
        navToDemo = navToDemo,
    )
    if (openDialog) {
        val authDialogLabels = getDialogLabels(type)
        AuthDialog(
            type = type,
            tips = if (authDialogLabels?.tips == null) "" else authDialogLabels.tips,
            usernamePlaceholder = authDialogLabels?.username,
            guideLink = authDialogLabels?.guideLink,
            onDismiss = {
                openDialog = false
            },
            onOpenGuideLink = {
                uriHandler.openUri(it)
            },
            onSuccess = {
                navToFeeds()
            })
    }
}

@Composable
private fun MainContent(
    onItemClick: (LoginRssModel) -> Unit,
    navToDemo: () -> Unit,
) {
    val items = LoginViewModel.SERVICE_LIST
    val index = remember {
        mutableIntStateOf(0)
    }
    val displayServices = remember {
        mutableStateOf(items.filter { it.group == RssTypeGroup.SERVICE })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_logo),
                contentDescription = "FeedUs logo",
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            contentAlignment = Alignment.TopCenter,
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(96.dp),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 24.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(20.dp))
                    .padding(vertical = 20.dp),
            ) {
                items(count = displayServices.value.size.orZero(), itemContent = {
                    RssServiceItem(displayServices.value[it], onItemClick = onItemClick)
                })
            }
            SingleChoiceSegmentedButtonRow {
                SegmentedButton(selected = index.intValue == 0, onClick = {
                    index.intValue = 0
                    displayServices.value = items.filter { it.group == RssTypeGroup.SERVICE }
                }, shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)) {
                    Text(text = "Service", style = MaterialTheme.typography.labelMedium)
                }
                SegmentedButton(selected = index.intValue == 1, onClick = {
                    index.intValue = 1
                    displayServices.value = items.filter { it.group == RssTypeGroup.SELF_HOST }
                }, shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)) {
                    Text(text = "Self Host", style = MaterialTheme.typography.labelMedium)
                }
                SegmentedButton(selected = index.intValue == 2, onClick = {
                    index.intValue = 2
                    displayServices.value = items.filter { it.group == RssTypeGroup.LOCAL }
                }, shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)) {
                    Text(text = "Local", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Text(
            text = stringResource(resource = Res.string.demo_title),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(16.dp).noRippleClickable {
                navToDemo()
            }
        )
        Text(
            text = "Since 2025",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun getDialogLabels(type: String): AuthDialogLabels? {
    return when (type) {
        Static.ACCOUNT_TYPE_FEEDLY,
        Static.ACCOUNT_TYPE_INOREADER_OAUTH2,
        Static.ACCOUNT_TYPE_FOLO -> null

        Static.ACCOUNT_TYPE_INOREADER -> AuthDialogLabels(
            stringResource(Res.string.login_username_inoreader),
            stringResource(Res.string.login_tip_inoreader),
            null,
        )

        Static.ACCOUNT_TYPE_BAZQUX_READER -> AuthDialogLabels(
            stringResource(Res.string.login_username_bazqux),
            stringResource(Res.string.login_tip_bazqux),
            null,
        )

        Static.ACCOUNT_TYPE_THE_OLD_READER -> AuthDialogLabels(
            stringResource(Res.string.login_username_the_old_reader),
            stringResource(Res.string.login_tip_the_old_reader),
            null,
        )

        Static.ACCOUNT_TYPE_FRESH_RSS -> AuthDialogLabels(
            stringResource(Res.string.login_username_fresh_rss),
            stringResource(Res.string.login_tip_fresh_rss),
            stringResource(Res.string.login_guide_link_fresh_rss),
        )

        Static.ACCOUNT_TYPE_GOOGLE_READER_API -> AuthDialogLabels(
            stringResource(Res.string.login_username_google_reader),
            stringResource(Res.string.login_tip_google_reader),
            null,
        )

        Static.ACCOUNT_TYPE_FEVER -> AuthDialogLabels(
            stringResource(Res.string.login_username_fever),
            stringResource(Res.string.login_tip_fever),
            null,
        )

        Static.ACCOUNT_TYPE_MINIFLUX -> AuthDialogLabels(
            stringResource(Res.string.login_username_fever),
            stringResource(Res.string.login_tip_fever),
            null,
        )

        Static.ACCOUNT_TYPE_FEEDBIN -> AuthDialogLabels(
            stringResource(Res.string.login_username_feedbin),
            stringResource(Res.string.login_tip_feedbin),
            null,
        )

        Static.ACCOUNT_TYPE_TTRSS -> AuthDialogLabels(
            stringResource(Res.string.login_username_ttrss),
            stringResource(Res.string.login_tip_ttrss),
            null,
        )

        else -> null
    }
}

private fun onItemClick(
    type: String,
    onShowDialog: () -> Unit,
    loginForLocalRss: () -> Unit,
    startOAuth: (accountType: String) -> Unit,
) {
    when (type) {
        Static.ACCOUNT_TYPE_FEEDLY,
        Static.ACCOUNT_TYPE_INOREADER_OAUTH2,
        Static.ACCOUNT_TYPE_FOLO -> startOAuth(type)
        Static.ACCOUNT_TYPE_LOCAL_RSS -> {
            loginForLocalRss()
        }

        else -> {
            onShowDialog()
        }
    }
}

@Composable
private fun RssServiceItem(model: LoginRssModel, onItemClick: (LoginRssModel) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable { onItemClick(model) }
    ) {
        Image(
            painter = painterResource(resource = model.logo ?: Res.drawable.ic_notification_logo),
            contentDescription = model.name,
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(8.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = model.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        model.tags?.forEach {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W600,
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenComposablePreview() {
    LoginScreenComposable(
        setTokenAccountType = {},
        navToFeeds = {},
        navToDemo = {},
        loginForLocalRss = {},
        startOAuth = {},
    )
}
