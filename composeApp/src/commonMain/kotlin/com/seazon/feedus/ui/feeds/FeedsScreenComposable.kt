package com.seazon.feedus.ui.feeds

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seazon.feedme.lib.rss.bo.Category
import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.Label
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.customize.Avatar
import com.seazon.feedus.ui.customize.noRippleClickable
import feedus.composeapp.generated.resources.Res
import feedus.composeapp.generated.resources.all_items
import feedus.composeapp.generated.resources.starred_items
import feedus.composeapp.generated.resources.tab_later
import feedus.composeapp.generated.resources.tab_new
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

private val ITEM_HEIGHT = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedsScreenComposable(
    stateFlow: StateFlow<FeedsScreenState>,
    navToArticles: (categoryId: String?, feedId: String?, starred: Boolean, labelId: String?) -> Unit,
    navToDemo: () -> Unit,
    sync: () -> Unit,
    logout: () -> Unit,
    onError: (message: String) -> Unit = {},
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    val state by stateFlow.collectAsState()
    val tabTitles = listOf(
        stringResource(resource = Res.string.tab_new),
        stringResource(resource = Res.string.tab_later)
    )
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        AppBar(state.serviceName, navToDemo, sync, logout, navToSubscribe = {
            openDialog = true
        })

        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = if (selectedTabIndex == index) {
                                MaterialTheme.typography.titleSmall
                            } else {
                                MaterialTheme.typography.bodyMedium
                            }
                        )
                    },
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTabIndex) {
                0 -> MainContent(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onFeedClick = {
                        if (!state.isSupportFetchByFeedOrCategory) {
                            onError("not support fetch by feed")
                            return@MainContent
                        }
                        navToArticles(null, it.id, false, null)
                    },
                    onCategoryClick = {
                        if (!state.isSupportFetchByFeedOrCategory) {
                            onError("not support fetch by category")
                            return@MainContent
                        }
                        navToArticles(it.id, null, false, null)
                    },
                    onAllClick = {
                        navToArticles(null, null, false, null)
                    },
                )

                1 -> ReadLaterContent(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onStarredClick = {
                        navToArticles(null, null, true, null)
                    },
                    onLabelClick = {
                        navToArticles(null, null, false, it.id)
                    }
                )
            }
        }
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
            ItemGroup(
                category = allItemsCategory,
                type = GroupType.ALL,
                showUnreadCount = true,
                expand = false,
                onItemClick = {
                    onAllClick()
                },
                onExpandClick = { e -> })
        }
        items(count = state.categories.size, itemContent = {
            ItemGroup(
                category = state.categories[it],
                type = GroupType.CATEGORY,
                showUnreadCount = state.showUnreadCount,
                expand = false,
                onItemClick = onCategoryClick,
                onExpandClick = { e ->
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
            ItemChild(feeds[it], state.showUnreadCount, onFeedClick)
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
private fun ReadLaterContent(
    modifier: Modifier = Modifier,
    state: FeedsScreenState,
    onStarredClick: () -> Unit,
    onLabelClick: (label: Label) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            )
            .padding(horizontal = 16.dp)
    ) {
        item {
            val starredItemsCategory = Category(
                id = "",
                title = stringResource(resource = Res.string.starred_items),
                sortId = null,
                cntClientAll = 0,
                cntClientUnread = state.starredCount,
            )
            ItemGroup(
                category = starredItemsCategory,
                type = GroupType.STARRED,
                showUnreadCount = true,
                expand = false,
                onItemClick = {
                    onStarredClick()
                },
                onExpandClick = { e -> })
        }
        items(count = state.labels.size, itemContent = {
            LabelItem(state.labels[it], onLabelClick)
        })
    }
}

@Composable
private fun ItemGroup(
    category: Category,
    type: GroupType,
//    feedConfig: FeedConfig,
    showUnreadCount: Boolean,
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
        if (type == GroupType.STARRED || showUnreadCount) {
            Text(
                text = "${category.cntClientUnread}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun ItemChild(
    feed: Feed,
    showUnreadCount: Boolean = false,
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
        if (showUnreadCount) {
            Text(
                text = "${feed.cntClientUnread}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun LabelItem(
    label: Label,
    onLabelClick: (label: Label) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(ITEM_HEIGHT)
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onLabelClick(label) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.AutoMirrored.Default.Label,
            contentDescription = "",
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            Text(
                modifier = Modifier,
                text = label.label.orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
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
            showUnreadCount = false,
            isSupportFetchByFeedOrCategory = false,
            feeds = feeds,
        )
    )
    FeedsScreenComposable(
        stateFlow = state,
        navToArticles = { categoryId, feedId, starred, labelId -> },
        navToDemo = {},
        logout = {},
        sync = {})
}

enum class GroupType {
    CATEGORY,
    ALL,
    STARRED,
}