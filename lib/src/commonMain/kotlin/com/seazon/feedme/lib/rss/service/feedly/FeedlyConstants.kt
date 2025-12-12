package com.seazon.feedme.lib.rss.service.feedly

import com.seazon.feedme.lib.rss.service.Static

object FeedlyConstants {
    const val EXPIRED_TIMESTAMP: Long = (7 * 24 * 3600).toLong() // seconds（2017/6/8）

    const val HTTP_CHARSET_PARAMETER: String = "http.protocol.content-charset"

    const val CLIENT_ID: String = "feedme"
    const val CLIENT_SECRET: String = "VVMDWBKUHTJM4WUQ20GVCGEM"

    const val SCHEMA_HTTPS: String = "https://feedly.com"
    const val API: String = "/v3"

    const val VALUE_SEPARATOR: String = "="
    const val AUTH: String =
        API + "/auth/auth?client_id=" + CLIENT_ID + "&redirect_uri=" + Static.REDIRECT_URI_OLD + "&scope=https://cloud.feedly.com/subscriptions&state=%s&response_type=code"
    const val TOKEN: String = "$API/auth/token"
    const val HTTP_HEADER_AUTHORIZATION_KEY: String = "Authorization"
    const val HTTP_HEADER_AUTHORIZATION_VALUE: String = "OAuth %s"

    const val GLOBAL_CATEGORY_MUST: String = "global.must"
    const val GLOBAL_CATEGORY_ALL: String = "global.all"
    const val GLOBAL_CATEGORY_UNCATEGORIZED: String = "global.uncategorized"

    const val GLOBAL_TAG_READ: String = "global.read"
    const val GLOBAL_TAG_SAVED: String = "global.saved"

    //
    const val URL_MARKERS: String = "$API/markers"
    const val URL_MARKERS_COUNTS: String = "$API/markers/counts"

    const val URL_PROFILE: String = "$API/profile"

    const val URL_SUBSCRIPTIONS: String = "$API/subscriptions"

    const val URL_CATEGORIES: String = "$API/categories"

    const val URL_STREAMS_IDS: String = "$API/streams/ids"
    const val URL_STREAMS_CONTENTS: String = "$API/streams/contents"

    const val URL_SEARCH_FEEDS: String = "$API/search/feeds"

    const val URL_ENTRIES_CONTENT: String = "$API/entries/%s"
    const val URL_ENTRIES_IDS: String = "$API/entries/.mget"

    const val URL_TAGS: String = "$API/tags"

    const val URL_ENTRIES: String = "$API/entries/.mget"

    fun isIgnoredTag(tag: String?): Boolean {
        tag ?: return true

        return tag.endsWith(GLOBAL_CATEGORY_ALL)
                || tag.endsWith(GLOBAL_CATEGORY_MUST)
                || tag.endsWith(GLOBAL_CATEGORY_UNCATEGORIZED)
    }

    fun isIgnoredForTag(tag: String?): Boolean {
        return tag == GLOBAL_TAG_SAVED || tag == GLOBAL_TAG_READ
    }
}
