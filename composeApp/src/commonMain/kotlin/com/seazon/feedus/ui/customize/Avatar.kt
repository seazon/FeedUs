package com.seazon.feedus.ui.customize

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.seazon.feedme.lib.utils.StringUtil
import feedus.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import java.io.File

/**
 * Avatar supports
 * - image from network (currently not support well, show empty if load failed)
 * - image from local file path(not drawable resource in project)
 * - first 2 letter of title
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: Dp,
    imageUrl: String?,
    title: String?,
) {
    Avatar(modifier = modifier, size = size, bgColor = Color.Black, textColor = Color.White, imageUrl = imageUrl, title = title)
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: Dp,
    bgColor: Color,
    textColor: Color,
    imageUrl: String?,
    title: String?,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape = RoundedCornerShape(percent = 100))
            .background(color = bgColor)
    ) {
        val contentDescription = title.orEmpty() + stringResource(resource = Res.string.category_feed)
        if (showImage(imageUrl)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    modifier = Modifier.semantics {
                        this.contentDescription = contentDescription
                    },
                    text = title.orEmpty().take(2), style = TextStyle(
                        color = textColor,
                        fontSize = TextUnit(size.value * 0.49f, TextUnitType.Sp), // 0.5 is too accurate, and 2 words cannot be displayed on one line on some models, so use 0.49
                        fontWeight = W900,
                    )
                )
            }
        }
    }
}

private fun showImage(imageUrl: String?): Boolean {
    if (imageUrl.isNullOrEmpty()) return false
    if (StringUtil.isHttpUrl(imageUrl)) return true
    return File(imageUrl).exists()
}

@Preview
@Composable
fun AvatarTextPreview() {
    Avatar(size = 40.dp, imageUrl = "", title = "少数")
}

@Preview
@Composable
fun AvatarImagePreview() {
    Avatar(
        size = 40.dp,
        imageUrl = "https://img1.baidu.com/it/u=2188421905,2216403526&fm=253&fmt=auto&app=138&f=PNG?w=500&h=500",
        title = ""
    )
}