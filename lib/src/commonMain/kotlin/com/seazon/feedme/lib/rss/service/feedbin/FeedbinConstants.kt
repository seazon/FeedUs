package com.seazon.feedme.lib.rss.service.feedbin

object FeedbinConstants {
    val EXPIRED_TIMESTAMP: Long = (10 * 365 * 24 * 3600 // 秒（2017/6/8记录） // 不会过期，设置了一个10年的过期时间
            ).toLong()

    const val SCHEMA_HTTPS: String = "https://api.feedbin.com"
    const val API: String = "/v2"

    val AUTH: String = API + "/authentication.json"
    const val HTTP_HEADER_AUTHORIZATION_KEY: String = "Authorization"
    const val HTTP_HEADER_AUTHORIZATION_VALUE: String = "Basic %s"


    val URL_SUBSCRIPTIONS: String = API + "/subscriptions.json"
    val URL_SUBSCRIPTIONS_DELETE: String = API + "/subscriptions/%s.json"
    val URL_UNREAD_ENTRIES: String = API + "/unread_entries.json"
    val URL_STARRED_ENTRIES: String = API + "/starred_entries.json"
    val URL_ENTRIES: String = API + "/entries.json"
    val URL_FEED_ENTRIES: String = API + "/feeds/%s/entries.json"
    val URL_TAGGINGS: String = API + "/taggings.json"
    val URL_TAGGING: String = API + "/taggings/%s.json"
}
