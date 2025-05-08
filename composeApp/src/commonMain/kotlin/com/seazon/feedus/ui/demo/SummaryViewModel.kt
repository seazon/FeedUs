package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.summary.SummaryUtil
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.launch

class SummaryViewModel(
) : BaseViewModel() {

    fun summary(
        type: String, key: String, query: String, lang: String,
    ) {
        viewModelScope.launch {
            try {
                val text = SummaryUtil.summary(query, lang, key, type)
                debug("text: $text")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}