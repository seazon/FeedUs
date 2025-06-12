package com.seazon.feedus.ui.articles

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.Item
import com.seazon.feedme.lib.utils.HtmlUtils
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedus.DateUtil
import com.seazon.feedus.ui.customize.EmptyView
import com.seazon.feedus.ui.customize.LoadingView
import com.seazon.feedus.ui.customize.noRippleClickable
import feedus.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArticlesScreenComposable(
    stateFlow: StateFlow<ArticlesScreenState>,
    onItemClick: (item: Item) -> Unit,
    navBack: () -> Unit,
    markAllRead: () -> Unit,
) {
    val state by stateFlow.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        AppBar(state, navBack, markAllRead)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(vertical = 4.dp)
        ) {
            if (state.items.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(count = state.items.size, itemContent = {
                        val item = state.items[it]
                        Item(item, state.feedMap[item.fid], onItemClick = onItemClick)
                    })
                }
            } else {
                EmptyView()
            }
            if (state.isLoading) {
                LoadingView()
            }
        }
    }
}

@Composable
fun AppBar(state: ArticlesScreenState, navBack: () -> Unit, markAllRead: () -> Unit) {
    val title = state.title ?: if (state.listType == ListType.STARRED)
        stringResource(Res.string.starred_items)
    else
        stringResource(Res.string.all_items)

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
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
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        elevation = 0.dp,
        actions = {
            IconButton(
                onClick = {
                    markAllRead()
                },
                modifier = Modifier.padding(start = 2.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}

@Composable
fun Item(item: Item, feed: Feed?, onItemClick: (item: Item) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .noRippleClickable {
                onItemClick(item)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        ) {
            if (!feed?.title.isNullOrEmpty()) {
                Text(
                    text = feed?.title.orEmpty(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            Spacer(modifier = Modifier.heightIn(4.dp))
            Text(
                text = HtmlUtils.decode(item.title.orEmpty()),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.heightIn(4.dp))
            Text(
                text = DateUtil.toXAgo(item.publishedDate.orZero()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }
        if (!item.visual.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp)),
                model = item.visual,
                contentDescription = "article thumbnail",
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview
@Composable
fun ArticlesScreenPreview() {
    val items = (0..10).map {
        Item(
            id = it.toString(),
            fid = "1",
            title = "[$it]this is title, it should be very very very very very very very very very very long",
            visual = "https://img.daofm.cn/wp-content/uploads/2020/09/Logo_Aroga-01.jpg",
            publishedDate = System.currentTimeMillis(),
        )
    }
    val feeds = listOf(
        Feed(
            id = "1",
            title = "dao",
        )
    )
    val state = MutableStateFlow(
        ArticlesScreenState(
            isLoading = false,
            items = items,
            feedMap = feeds.associateBy { it.id }
        )
    )
    ArticlesScreenComposable(state, onItemClick = {}, navBack = {}, markAllRead = {})
}