package com.seazon.feedus.ui.customize

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FmTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    hint: String = "",
//    singleLine: Boolean = true,
//    focusCb: ((Boolean) -> Unit)? = null,
//    horizontalPadding: Dp = 0.dp,
//    verticalPadding: Dp = 0.dp,
    imageVector: ImageVector? = null,
    reverse: Boolean = false,
//    iconSpacing: Dp = 16.dp,
//    iconPadding: Dp = 0.dp,
//    showClearImg: Boolean = true,
//    enabled: Boolean = true,
//    readOnly: Boolean = false,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    // 焦点, 用于控制是否显示 右侧叉号
    val hasFocus = remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = if (reverse) TextAlign.End else TextAlign.Start,
    )
    BasicTextField(value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 0.dp) // default is 8dp, so set to 0dp
            .onFocusChanged {
                hasFocus.value = it.isFocused
//                focusCb?.invoke(it.isFocused)
            },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
//        enabled = enabled,
//        readOnly = readOnly,
        textStyle = textStyle,
//        keyboardOptions = KeyboardOptions.Default. . keyboardOptions,
//        keyboardActions = KeyboardActions.Default.onSearch,
//        visualTransformation = visualTransformation,
//        cursorBrush = cursorBrush,
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                if (startLayout != null) {
//                    startLayout.invoke()
//                } else
                if (imageVector != null) {
                    Image(
                        imageVector = imageVector,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                    )
                } else {
                    Spacer(modifier = Modifier.width(24.dp))
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = if (reverse) Alignment.CenterEnd else Alignment.CenterStart,
                ) {
                    // 当空字符时, 显示hint
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = textStyle
                        )
                    }
                    // 原本输入框的内容
                    innerTextField()
                }

                // 存在焦点 且 有输入内容时. 显示叉号
                if (hasFocus.value && value.isNotEmpty()) {
                    Image(imageVector = Icons.Sharp.Clear,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)
                            .noRippleClickable { onValueChange.invoke("") })
                } else {
                    Spacer(modifier = Modifier.width(24.dp))
                }
            }
        })
}

@Preview
@Composable
fun FmTextFieldDarkPreview() {
    FmTextField(
        modifier = Modifier,
        value = "hello",
        onValueChange = {},
    )
}

@Preview
@Composable
fun FmTextFieldLightPreview() {
    FmTextField(
        value = "2222233333szdfasdf",
        hint = "this is hint",
        onValueChange = {},
        reverse = true,
        imageVector = Icons.Default.Check
    )
}
