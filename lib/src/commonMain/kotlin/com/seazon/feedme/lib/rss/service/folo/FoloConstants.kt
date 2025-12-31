package com.seazon.feedme.lib.rss.service.folo

object FoloConstants {
    const val EXPIRED_TIMESTAMP: Long = (7 * 24 * 3600).toLong() // seconds
    const val SCHEMA_HTTPS: String = "https://api.folo.is"
    const val API: String = ""
    const val TOKEN: String = "/better-auth/one-time-token/apply"
    const val HTTP_HEADER_AUTHORIZATION_KEY: String = "Authorization"
    const val URL_READS: String = "$API/reads"
    const val URL_SUBSCRIPTIONS: String = "$API/subscriptions"
    const val URL_COLLECTIONS: String = "$API/collections"
    const val URL_FEEDS: String = "$API/feeds"
    const val URL_DISCOVER: String = "$API/discover"
    const val URL_ENTRIES: String = "$API/entries"
}
