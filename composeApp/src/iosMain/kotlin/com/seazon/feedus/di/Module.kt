package com.seazon.feedus.di

import com.seazon.feedus.cache.DatabaseDriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseDriverFactory> { DatabaseDriverFactory() }
}
