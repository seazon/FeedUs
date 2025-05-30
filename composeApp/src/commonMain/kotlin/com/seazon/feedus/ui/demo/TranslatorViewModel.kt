package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.translation.TranslatorUtil
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.launch

class TranslatorViewModel(
) : BaseViewModel() {

    fun translate(
        type: String, key: String, appId: String, query: String, lang: String,
    ) {
        viewModelScope.launch {
            try {
                val text = TranslatorUtil.translate(query, lang, key, appId, type)
                debug("text: $text")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}