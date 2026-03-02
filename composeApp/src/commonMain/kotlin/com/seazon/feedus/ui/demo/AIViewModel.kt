package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.ai.AIModel
import com.seazon.feedme.lib.ai.AiException
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

    fun query(type: AIModel, baseUrl: String, key: String, model: String, query: String, lang: String, prompt: String) {
        _state.update {
            it.copy(
                loading = true,
                output = null,
            )
        }
        viewModelScope.launch {
            try {
                val text = GeneralAIApi().text2Text(type, baseUrl, key, model, prompt, query, lang)
                debug("text: $text")
                _state.update {
                    it.copy(
                        loading = false,
                        output = text,
                    )
                }
            } catch (e: AiException) {
                e.printStackTrace()
                _state.update {
                    it.copy(
                        loading = false,
                        output = e.message,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update {
                    it.copy(
                        loading = false,
                    )
                }
            }
        }
    }
}