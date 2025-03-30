package com.seazon.feedus

import androidx.compose.ui.window.ComposeUIViewController
import com.seazon.feedus.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(
                listOf(
                    platformModule,
                    appModule,
                )
            )
        }
    }
) { App() }