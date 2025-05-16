package com.seazon.feedus.ui.demo

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.seazon.feedme.lib.summary.SummaryUtil
import com.seazon.feedus.ui.customize.FmLabel
import com.seazon.feedus.ui.customize.FmPrimaryButton
import com.seazon.feedus.ui.customize.FmTextField
import feedus.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun SummaryScreenComposable(
    summary: (type: String, key: String, query: String) -> Unit,
) {
    val typeList = mutableListOf(
        SummaryUtil.TYPE_GEMINI,
    )
    val title = stringResource(Res.string.summary_title)
    val typeLabel = stringResource(Res.string.translator_type)
    val keyLabel = stringResource(Res.string.translator_key)
    val typeValue = remember { mutableStateOf(SummaryUtil.TYPE_GEMINI) }
    val keyValue = remember { mutableStateOf("") }
    val queryValue = remember { mutableStateOf("") }
    val expanded = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        // title
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
        // type
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = typeLabel,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.CenterEnd) {
                Box {
                    Row(
                        modifier = Modifier.clickable {
                            expanded.value = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FmLabel(
                            text = typeValue.value,
                        )
                        Image(
                            imageVector = Icons.Filled.ExpandMore,
                            modifier = Modifier
                                .size(56.dp)
                                .padding(16.dp),
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                            contentDescription = "dropdown arrow"
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
                        expanded = expanded.value,
                        onDismissRequest = {
                            expanded.value = false
                        },
                        content = {
                            typeList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded.value = false
                                        typeValue.value = it
                                    },
                                    text = {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    }
                                )
                            }
                        },
                    )
                }
            }
        }
        // key
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = keyLabel,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FmTextField(
                value = keyValue.value,
                modifier = Modifier.weight(3f),
                reverse = true,
                onValueChange = {
                    keyValue.value = it
                },
            )
        }
        // query
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = "query",
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FmTextField(
                value = queryValue.value,
                modifier = Modifier.weight(3f),
                reverse = true,
                onValueChange = {
                    queryValue.value = it
                },
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        FmPrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "summary",
            onClick = {
                summary(typeValue.value, keyValue.value, queryValue.value)
            }
        )
    }
}

@Preview
@Composable
fun SummaryScreenComposablePreview() {
    SummaryScreenComposable(summary = { type, key, query -> })
}
