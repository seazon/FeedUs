package com.seazon.feedus

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.seazon.feedus.di.appModule
import com.seazon.feedus.di.platformModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(
            listOf(
                platformModule,
                appModule,
            )
        )
    }
    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FeedUs",
        ) {
            App()
        }
    }
}