package com.seazon.feedus.ui.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.seazon.feedme.lib.LocalConstants
import com.seazon.feedme.lib.ai.AIGenerationConfig
import com.seazon.feedme.lib.ai.AIModel
import com.seazon.feedme.lib.ai.Prompt
import com.seazon.feedus.ui.customize.FmLabel
import com.seazon.feedus.ui.customize.FmPrimaryButton
import com.seazon.feedus.ui.customize.FmTextField
import feedus.composeapp.generated.resources.Res
import feedus.composeapp.generated.resources.ai_base_url
import feedus.composeapp.generated.resources.ai_key
import feedus.composeapp.generated.resources.ai_model
import feedus.composeapp.generated.resources.ai_prompt
import feedus.composeapp.generated.resources.ai_query
import feedus.composeapp.generated.resources.ai_title
import feedus.composeapp.generated.resources.ai_type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AIScreenComposable(
    stateFlow: StateFlow<AIScreenState>,
    query: (type: AIModel, baseUrl: String, key: String, model: String, query: String, prompt: String) -> Unit,
) {
    val state by stateFlow.collectAsState()
    val typeList = AIModel.entries.toTypedArray()

    var keyValue by remember { mutableStateOf(LocalConstants.KEY_VALUE) }

    var typeValue by remember { mutableStateOf(AIModel.Gemini) }
    var typeExpanded by rememberSaveable { mutableStateOf(false) }

    var config by remember { mutableStateOf(AIGenerationConfig.getConfig(typeValue)) }

    var baseUrlValue by remember { mutableStateOf(config.apiUrl) }

    var modelList by remember { mutableStateOf(config.modelList) }
    var modelValue by remember { mutableStateOf(config.modelList.first()) }
    var modelExpanded by rememberSaveable { mutableStateOf(false) }

    var promptList by remember { mutableStateOf(Prompt.configs) }
    var promptValue by remember { mutableStateOf(Prompt.configs.first()) }
    var promptExpanded by rememberSaveable { mutableStateOf(false) }

    var queryValue by remember { mutableStateOf(LocalConstants.QUERY_VALUE) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        // title
        Text(
            text = stringResource(Res.string.ai_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        // type
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = stringResource(Res.string.ai_type),
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.CenterEnd) {
                Box {
                    Row(
                        modifier = Modifier.clickable {
                            typeExpanded = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FmLabel(
                            text = typeValue.name,
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
                        expanded = typeExpanded,
                        onDismissRequest = {
                            typeExpanded = false
                        },
                        content = {
                            typeList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        typeExpanded = false
                                        typeValue = it
                                        config = AIGenerationConfig.getConfig(typeValue)
                                        baseUrlValue = config.apiUrl
                                        modelList = config.modelList
                                        modelValue = config.modelList.firstOrNull().orEmpty()
                                    },
                                    text = {
                                        Text(
                                            text = it.name,
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
        // base url
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = stringResource(Res.string.ai_base_url),
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FmTextField(
                value = baseUrlValue,
                modifier = Modifier.weight(3f),
                reverse = true,
                enabled = config.urlEditable,
                onValueChange = {
                    baseUrlValue = it
                },
            )
        }
        // model
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = stringResource(Res.string.ai_model),
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (typeValue == AIModel.Custom) {
                Column(modifier = Modifier.weight(3f)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FmTextField(
                        value = modelValue,
                        reverse = true,
                        onValueChange = {
                            modelValue = it
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.CenterEnd) {
                    Box {
                        Row(
                            modifier = Modifier.clickable {
                                modelExpanded = true
                            },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FmLabel(
                                text = modelValue,
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
                            expanded = modelExpanded,
                            onDismissRequest = {
                                modelExpanded = false
                            },
                            content = {
                                modelList.forEach {
                                    DropdownMenuItem(
                                        onClick = {
                                            modelExpanded = false
                                            modelValue = it
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
        }
        // key
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = stringResource(Res.string.ai_key),
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FmTextField(
                value = keyValue,
                modifier = Modifier.weight(3f),
                reverse = true,
                onValueChange = {
                    keyValue = it
                },
            )
        }
        // prompt
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = stringResource(Res.string.ai_prompt),
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.CenterEnd) {
                Box {
                    Row(
                        modifier = Modifier.clickable {
                            promptExpanded = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FmLabel(
                            text = promptValue.type.name,
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
                        expanded = promptExpanded,
                        onDismissRequest = {
                            promptExpanded = false
                        },
                        content = {
                            promptList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        promptExpanded = false
                                        promptValue = it
                                    },
                                    text = {
                                        Text(
                                            text = it.type.name,
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
        // query
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = "query",
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FmTextField(
                value = queryValue,
                modifier = Modifier.weight(3f),
                reverse = true,
                onValueChange = {
                    queryValue = it
                },
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (state.loading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        } else {
            FmPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.ai_query),
                onClick = {
                    query(
                        typeValue,
                        baseUrlValue,
                        keyValue,
                        modelValue,
                        queryValue,
                        promptValue.content,
                    )
                }
            )
        }
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
fun AIScreenComposablePreview() {
    val stateFlow = MutableStateFlow(AIScreenState(false, "this is output"))
    AIScreenComposable(stateFlow = stateFlow, query = { type, baseUrl, key, model, query, prompt -> })
}
