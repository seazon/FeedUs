package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.ai.AIModel
import com.seazon.feedme.lib.ai.GeneralAIApi
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AIViewModel() : BaseViewModel() {

    private val _state = MutableStateFlow(AIScreenState())
    val state: StateFlow<AIScreenState> = _state

    fun query(type: AIModel, key: String, model: String, query: String, lang: String, prompt: String) {
        viewModelScope.launch {
            try {
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