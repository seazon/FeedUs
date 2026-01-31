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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.seazon.feedme.lib.LocalConstants
import com.seazon.feedme.lib.ai.gemini.GeminiApi
import com.seazon.feedme.lib.summary.SummaryUtil
import com.seazon.feedus.ui.customize.FmLabel
import com.seazon.feedus.ui.customize.FmPrimaryButton
import com.seazon.feedus.ui.customize.FmTextField
import feedus.composeapp.generated.resources.Res
import feedus.composeapp.generated.resources.summary_title
import feedus.composeapp.generated.resources.translator_key
import feedus.composeapp.generated.resources.translator_model
import feedus.composeapp.generated.resources.translator_type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

@Composable
fun SummaryScreenComposable(
    stateFlow: StateFlow<SummaryScreenState>,
    summary: (type: String, key: String, model: String, query: String) -> Unit,
) {
    val state by stateFlow.collectAsState()
    val typeList = mutableListOf(
        SummaryUtil.TYPE_GEMINI,
    )
    val modelList = GeminiApi.MODELS
    val title = stringResource(Res.string.summary_title)
    val typeLabel = stringResource(Res.string.translator_type)
    val keyLabel = stringResource(Res.string.translator_key)
    val modelLabel = stringResource(Res.string.translator_model)
    val typeValue = remember { mutableStateOf(SummaryUtil.TYPE_GEMINI) }
    val keyValue = remember { mutableStateOf(LocalConstants.KEY_VALUE) }
    val modelValue = remember { mutableStateOf(modelList.first()) }
    val queryValue = remember { mutableStateOf("") }
    val typeExpanded = rememberSaveable { mutableStateOf(false) }
    val modelExpanded = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .systemBarsPadding()
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
                            typeExpanded.value = true
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
                        expanded = typeExpanded.value,
                        onDismissRequest = {
                            typeExpanded.value = false
                        },
                        content = {
                            typeList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        typeExpanded.value = false
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
        // model
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = modelLabel,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.CenterEnd) {
                Box {
                    Row(
                        modifier = Modifier.clickable {
                            modelExpanded.value = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FmLabel(
                            text = modelValue.value,
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
                        expanded = modelExpanded.value,
                        onDismissRequest = {
                            modelExpanded.value = false
                        },
                        content = {
                            modelList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        modelExpanded.value = false
                                        modelValue.value = it
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
                summary(typeValue.value, keyValue.value, modelValue.value, queryValue.value)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = state.output.orEmpty(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview
@Composable
fun SummaryScreenComposablePreview() {
    val stateFlow = MutableStateFlow(SummaryScreenState("this is output"))
    SummaryScreenComposable(stateFlow = stateFlow, summary = { type, key, model, query -> })
}
