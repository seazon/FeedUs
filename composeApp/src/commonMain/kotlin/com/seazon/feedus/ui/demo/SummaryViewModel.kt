package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.ai.AIModel
import com.seazon.feedme.lib.ai.GeneralAIApi
import com.seazon.feedme.lib.ai.Prompt
import com.seazon.feedme.lib.ai.PromptType
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SummaryViewModel() : BaseViewModel() {

    private val _state = MutableStateFlow(SummaryScreenState())
    val state: StateFlow<SummaryScreenState> = _state

    fun summary(type: AIModel, key: String, model: String, query: String, lang: String) {
        viewModelScope.launch {
            try {
                val prompt = Prompt.configs.first { it.type == PromptType.Summary }
                val text = GeneralAIApi().text2Text(type, key, model, prompt, query, lang)
                debug("text: $text")
                _state.update {
                    it.copy(
                        output = text,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}