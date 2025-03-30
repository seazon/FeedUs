package com.seazon.feedus

import android.app.Application
import com.seazon.feedus.di.appModule
import com.seazon.feedus.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(
                    platformModule,
                    appModule,
                )
            )
        }
    }
}