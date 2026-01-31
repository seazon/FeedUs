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
import com.seazon.feedme.lib.ai.AIGenerationConfig
import com.seazon.feedme.lib.ai.AIModel
import com.seazon.feedme.lib.ai.Prompt
import com.seazon.feedus.ui.customize.FmLabel
import com.seazon.feedus.ui.customize.FmPrimaryButton
import com.seazon.feedus.ui.customize.FmTextField
import feedus.composeapp.generated.resources.Res
import feedus.composeapp.generated.resources.ai_query
import feedus.composeapp.generated.resources.ai_title
import feedus.composeapp.generated.resources.ai_key
import feedus.composeapp.generated.resources.ai_model
import feedus.composeapp.generated.resources.ai_prompt
import feedus.composeapp.generated.resources.ai_type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

@Composable
fun AIScreenComposable(
    stateFlow: StateFlow<AIScreenState>,
    query: (type: AIModel, key: String, model: String, query: String, prompt: String) -> Unit,
) {
    val state by stateFlow.collectAsState()
    val typeList = mutableListOf(
        AIModel.Gemini,
        AIModel.Volces,
        AIModel.OpenAI,
        AIModel.Ernie,
        AIModel.QWen,
        AIModel.Dream,
        AIModel.Spark,
        AIModel.Claude,
        AIModel.GLM,
    )

    val keyValue = remember { mutableStateOf(LocalConstants.KEY_VALUE) }

    val typeValue = remember { mutableStateOf(AIModel.Gemini) }
    val typeExpanded = rememberSaveable { mutableStateOf(false) }

    val config: AIGenerationConfig = AIGenerationConfig.getConfig(typeValue.value)
    val modelList = remember { mutableStateOf(config.modelList) }
    val modelValue = remember { mutableStateOf(config.modelList.first()) }
    val modelExpanded = rememberSaveable { mutableStateOf(false) }

    val promptList = remember { mutableStateOf(Prompt.configs) }
    val promptValue = remember { mutableStateOf(Prompt.configs.first()) }
    val promptExpanded = rememberSaveable { mutableStateOf(false) }

    val queryValue = remember { mutableStateOf(LocalConstants.QUERY_VALUE) }
    Column(
        modifier = Modifier
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
        // type
        Spacer(modifier = Modifier.height(16.dp))
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
                            typeExpanded.value = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FmLabel(
                            text = typeValue.value.name,
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
                                        val config = AIGenerationConfig.getConfig(typeValue.value)
                                        modelList.value = config.modelList
                                        modelValue.value = config.modelList.first()
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
        // model
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FmLabel(
                text = stringResource(Res.string.ai_model),
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
                            modelList.value.forEach {
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
                text = stringResource(Res.string.ai_key),
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
                            promptExpanded.value = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FmLabel(
                            text = promptValue.value.type.name,
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
                        expanded = promptExpanded.value,
                        onDismissRequest = {
                            promptExpanded.value = false
                        },
                        content = {
                            promptList.value.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        promptExpanded.value = false
                                        promptValue.value = it
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
        Spacer(modifier = Modifier.height(16.dp))
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
            text = stringResource(Res.string.ai_query),
            onClick = {
                query(
                    typeValue.value,
                    keyValue.value,
                    modelValue.value,
                    queryValue.value,
                    promptValue.value.content,
                )
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
fun AIScreenComposablePreview() {
    val stateFlow = MutableStateFlow(AIScreenState("this is output"))
    AIScreenComposable(stateFlow = stateFlow, query = { type, key, model, query, prompt -> })
}
