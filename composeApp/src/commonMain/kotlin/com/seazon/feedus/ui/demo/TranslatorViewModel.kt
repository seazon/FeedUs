package com.seazon.feedus.ui.demo

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.translation.TranslatorUtil
import com.seazon.feedus.ui.BaseViewModel
import kotlinx.coroutines.launch

class TranslatorViewModel(
) : BaseViewModel() {

    fun translate(
        type: String, key: String, appId: String, query: String, lang: String,
    ) {
        viewModelScope.launch {
            println("asdfasdfa 1")
            try {
                val text = TranslatorUtil.translate(query, lang, key, appId, type)
                println("asdfasdfa text: $text")
            } catch (e: Exception) {
                println("asdfasdfa exception: $e")
                e.printStackTrace()
            }
        }
    }
}