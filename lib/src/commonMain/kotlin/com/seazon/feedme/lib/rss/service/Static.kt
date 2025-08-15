package com.seazon.feedme.lib.rss.service

import kotlinx.serialization.json.Json

object Static {

    val defaultJson = Json { ignoreUnknownKeys = true }

    const val ACCOUNT_TYPE_FEEDLY = "Feedly"
    const val ACCOUNT_TYPE_BAZQUX_READER = "BazQux Reader"
    const val ACCOUNT_TYPE_FEEDBIN = "Feedbin"
    const val ACCOUNT_TYPE_FEVER = "Fever"
    const val ACCOUNT_TYPE_TTRSS = "Tiny Tiny RSS"
    const val ACCOUNT_TYPE_INOREADER_OAUTH2 = "InoReaderOAuth2"
    const val ACCOUNT_TYPE_INOREADER = "InoReader"
    const val ACCOUNT_TYPE_THE_OLD_READER = "The Old Reader"
    const val ACCOUNT_TYPE_FRESH_RSS = "FreshRSS"
    const val ACCOUNT_TYPE_GOOGLE_READER_API = "Google Reader API"
    const val ACCOUNT_TYPE_LOCAL_RSS = "Local RSS"
    const val ACCOUNT_TYPE_MINIFLUX = "Miniflux"
    const val ACCOUNT_TYPE_FOLO = "Folo"

    const val REDIRECT_URI_OLD: String = "feedme://oauth"

    const val REDIRECT_URI: String = "feedme://oauth" //    public static final String URI_INDEX = "https://github.com/seazon/FeedMe/blob/master/README.md";
}
