package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.ai.AIModel
import com.seazon.feedme.lib.ai.AiException
import com.seazon.feedme.lib.ai.GeneralAIApi
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AIViewModel : BaseViewModel() {

    private val _state = MutableStateFlow(AIScreenState())
    val state: StateFlow<AIScreenState> = _state

    private var queryJob: Job? = null

    fun query(type: AIModel, baseUrl: String, key: String, model: String, query: String, lang: String, prompt: String) {
        queryJob?.cancel()
        _state.update {
            it.copy(
                loading = true,
                output = "",
            )
        }
        queryJob = viewModelScope.launch {
            GeneralAIApi().text2TextStream(type, baseUrl, key, model, prompt, query, lang)
                .onCompletion { cause ->
                    if (cause is CancellationException) return@onCompletion
                    debug("text: ${_state.value.output}")
                    _state.update {
                        it.copy(
                            loading = false,
                        )
                    }
                }
                .catch { e ->
                    e.printStackTrace()
                    _state.update {
                        it.copy(
                            output = e.message,
                        )
                    }
                }
                .collect { chunk ->
                    _state.update {
                        it.copy(
                            output = it.output.orEmpty() + chunk,
                        )
                    }
                }
        }
    }

    fun test(type: AIModel, baseUrl: String, key: String, model: String) {
        _state.update {
            it.copy(
                loading = true,
                output = null,
            )
        }
        viewModelScope.launch {
            try {
                val text = GeneralAIApi().test(type, baseUrl, key, model)
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