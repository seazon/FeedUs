package com.seazon.feedus.ui.feeds

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seazon.feedme.lib.rss.bo.Category
import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.customize.Avatar
import com.seazon.feedus.ui.customize.noRippleClickable
import feedus.composeapp.generated.resources.Res
import feedus.composeapp.generated.resources.all_items
import feedus.composeapp.generated.resources.starred_items
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

private val ITEM_HEIGHT = 48.dp

@Composable
fun FeedsScreenComposable(
    stateFlow: StateFlow<FeedsScreenState>,
    navToArticles: (categoryId: String?, feedId: String?, starred: Boolean) -> Unit,
    navToDemo: () -> Unit,
    sync: () -> Unit,
    isSupportFetchByFeedOrCategory: () -> Boolean,
    logout: () -> Unit,
    onError: (message: String) -> Unit = {},
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    val state by stateFlow.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        AppBar(state.serviceName, navToDemo, sync, logout, navToSubscribe = {
            openDialog = true
        })
        MainContent(
            modifier = Modifier.fillMaxWidth().weight(1f),
            state = state,
            onFeedClick = {
                if (!isSupportFetchByFeedOrCategory()) {
                    onError("not support fetch by feed")
                    return@MainContent
                }
                navToArticles(null, it.id, false)
            },
            onCategoryClick = {
                if (!isSupportFetchByFeedOrCategory()) {
                    onError("not support fetch by category")
                    return@MainContent
                }
                navToArticles(it.id, null, false)
            },
            onStarredClick = {
                navToArticles(null, null, true)
            },
            onAllClick = {
                navToArticles(null, null, false)
            },
        )
    }
    if (openDialog) {
        SubscribeDialog(
            onDismiss = {
                openDialog = false
            },
            onSuccess = {
                // TODO should handle subscribe success case
                openDialog = false
                debug("subscribe ok")
            }
        )
    }
}

@Composable
private fun AppBar(
    serviceName: String,
    navToDemo: () -> Unit,
    sync: () -> Unit,
    logout: () -> Unit,
    navToSubscribe: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = serviceName,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        elevation = 0.dp,
        actions = {
            IconButton(
                onClick = {
                    sync()
                },
                modifier = Modifier.padding(start = 2.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(
                onClick = {
                    logout()
                },
                modifier = Modifier.padding(start = 2.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(
                onClick = {
                    navToDemo()
                },
                modifier = Modifier.padding(start = 2.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeveloperMode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(
                onClick = {
                    navToSubscribe()
                },
                modifier = Modifier.padding(start = 2.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    state: FeedsScreenState,
    onFeedClick: (Feed) -> Unit,
    onCategoryClick: (Category) -> Unit,
    onAllClick: () -> Unit,
    onStarredClick: () -> Unit,
) {
    val feeds = state.feeds
    LazyColumn(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            )
            .padding(horizontal = 16.dp)
    ) {
//        val list = state?.data?.rootNode?.children
        item {
            val allItemsCategory = Category(
                id = "",
                title = stringResource(resource = Res.string.all_items),
                sortId = null,
                cntClientAll = 0,
                cntClientUnread = state.maxUnreadCount,
            )
            ItemGroup(allItemsCategory, GroupType.ALL, false, onItemClick = {
                onAllClick()
            }, onExpandClick = { e -> })
        }
        item {
            val starredItemsCategory = Category(
                id = "",
                title = stringResource(resource = Res.string.starred_items),
                sortId = null,
                cntClientAll = 0,
                cntClientUnread = state.starredCount,
            )
            ItemGroup(starredItemsCategory, GroupType.STARRED, false, onItemClick = {
                onStarredClick()
            }, onExpandClick = { e -> })
        }
        items(count = state.categories.size, itemContent = {
            ItemGroup(state.categories[it], GroupType.CATEGORY, false, onCategoryClick, onExpandClick = { e ->
//                expandListState.value = expandListState.value.toMutableList().apply {
//                    if (this.size > it) {
//                        this[it] = !e
//                    }
//                }.toList()
            })
        })
        items(count = state.feeds.size, itemContent = {
//            val expand = expandListState.value.getOrNull(it)
//            val groupItem = list?.getOrNull(it) ?: return@items
//            val feedConfig = state.getFeedConfigById(groupItem.id) ?: return@items

//            if (expand == null) {
//                if (feedConfig.type == FeedConfig.TYPE_FEED) {
            ItemChild(feeds[it], onFeedClick)
//                }
//            } else {
//                ItemGroup(groupItem, feedConfig, expand, onItemClick, onExpandClick = { e ->
//                    expandListState.value = expandListState.value.toMutableList().apply {
//                        if (this.size > it) {
//                            this[it] = !e
//                        }
//                    }.toList()
//                })
//                if (expand) {
//                    groupItem.children.forEach { childItem ->
//                        val childConfig = state.getFeedConfigById(childItem.id) ?: return@forEach
//                        ItemChild(childItem, childConfig, onItemClick)
//                    }
//                }
//            }
        })
    }
}

@Composable
private fun ItemGroup(
    category: Category,
    type: GroupType,
//    feedConfig: FeedConfig,
    expand: Boolean,
    onItemClick: (outline: Category) -> Unit,
    onExpandClick: (expand: Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(ITEM_HEIGHT)
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onItemClick(category) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val rotationValue by animateFloatAsState(targetValue = if (expand) -90f else 0f, label = "expandRotation")
        Image(
            imageVector = when (type) {
                GroupType.ALL -> Icons.Filled.Home
                GroupType.STARRED -> Icons.Filled.Star
                GroupType.CATEGORY -> Icons.Filled.Folder
            },
            contentDescription = "",
//            painter = painterResource(resource = Res.drawable.ic_keyboard_arrow_down_black_24dp),
//            contentDescription = stringResource(resource = Res.string.ui_artlist_expand),
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp)
                .graphicsLayer {
                    rotationZ = rotationValue
                }
                .noRippleClickable {
                    onExpandClick(expand)
                },
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            Text(
                modifier = Modifier,
                text = category.title.orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = "${category.cntClientUnread}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
private fun ItemChild(
    feed: Feed,
//    feedConfig: FeedConfig,
    onItemClick: (outline: Feed) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(ITEM_HEIGHT)
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onItemClick(feed) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Avatar(
            size = 24.dp,
            bgColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
//            imageUrl = FaviconUtil.getLocalFaviconsUrl(feed.id),
            imageUrl = feed.favicon,
            title = feed.title
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            Text(
                modifier = Modifier,
                text = feed.title.orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "${feed.cntClientUnread}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Preview
@Composable
fun FeedsScreenComposablePreview() {
    val feeds = (0..10).map {
        Feed(
            id = it.toString(),
            title = "I'm a feed with long long long long long long long long long long title $it",
        )
    }
    val state = MutableStateFlow(
        FeedsScreenState(
            feeds = feeds,
        )
    )
    FeedsScreenComposable(
        stateFlow = state,
        navToArticles = { categoryId, feedId, starred -> },
        navToDemo = {},
        logout = {},
        isSupportFetchByFeedOrCategory = { false },
        sync = {})
}

enum class GroupType {
    CATEGORY,
    ALL,
    STARRED,
}