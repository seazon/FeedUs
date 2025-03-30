package com.seazon.feedus.di

import com.seazon.feedus.cache.RssDatabase
import com.seazon.feedus.platform.DatabaseDriverFactory
import com.seazon.feedus.data.AppSettings
import com.seazon.feedus.data.RssSDK
import com.seazon.feedus.data.TokenSettings
import com.seazon.feedus.ui.articles.ArticlesViewModel
import com.seazon.feedus.ui.demo.TranslatorViewModel
import com.seazon.feedus.ui.feeds.FeedsViewModel
import com.seazon.feedus.ui.login.LoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TokenSettings() }
    single { AppSettings() }
    single {
        RssDatabase(
            sqlDriver = get<DatabaseDriverFactory>().createDriver()
        )
    }
    single { RssSDK(tokenSettings = get()) }
    single { LoginViewModel(rssSDK = get(), tokenSettings = get()) }
    single { TranslatorViewModel() }
    single { FeedsViewModel(rssSDK = get(), tokenSettings = get(), rssDatabase = get(), appSettings = get()) }
    viewModel { ArticlesViewModel(rssSDK = get(), tokenSettings = get(), rssDatabase = get()) }
}

expect val platformModule: Module