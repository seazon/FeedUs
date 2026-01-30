package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.summary.SummaryUtil
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SummaryViewModel() : BaseViewModel() {

    private val _state = MutableStateFlow(SummaryScreenState())
    val state: StateFlow<SummaryScreenState> = _state

    fun summary(
        type: String, key: String, model: String, query: String, lang: String,
    ) {
        viewModelScope.launch {
            try {
                val text = SummaryUtil.summary(query, lang, key, model, type)
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