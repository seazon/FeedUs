package com.seazon.feedus.di

import com.seazon.feedus.platform.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseDriverFactory> { DatabaseDriverFactory(context = androidContext()) }
}