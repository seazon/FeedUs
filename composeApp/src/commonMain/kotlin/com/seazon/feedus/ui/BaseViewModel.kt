package com.seazon.feedus.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    suspend fun <T> process(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }
}