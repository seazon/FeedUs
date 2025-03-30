package com.seazon.feedme.lib.rss.service.gr

import com.seazon.feedme.lib.rss.service.Static

object GrConstants {
    const val HTTP_CHARSET_PARAMETER: String = "http.protocol.content-charset"

    const val API: String = "/reader/api/0"

    const val VALUE_SEPARATOR: String = "="
    const val AUTH: String = "/accounts/ClientLogin"
    const val HTTP_HEADER_AUTHORIZATION_KEY: String = "Authorization"
    const val HTTP_HEADER_AUTHORIZATION_VALUE: String = "GoogleLogin auth=%s"
    const val HTTP_HEADER_AUTHORIZATION_VALUE_OAUTH2: String = "Bearer %s"
    const val HTTP_HEADER_USER_AGENT_KEY: String = "User-Agent"
    const val HTTP_HEADER_USER_AGENT_VALUE: String = "FeedMe"

    const val GLOBAL_STATE_STARRED: String = "com.google/starred"
    const val GLOBAL_STATE_READING_LIST: String = "com.google/reading-list"
    const val GLOBAL_STATE_READ: String = "com.google/read"

    const val GLOBAL_STATE_FRESH: String = "com.google/fresh"
    const val GLOBAL_STATE_BLOGGER_FOLLOWING: String = "com.google/blogger-following"
    const val GLOBAL_STATE_BROADCAST: String = "com.google/broadcast"

    const val TAG_READ: String = "user/-/state/com.google/read"
    const val TAG_STARRED: String = "user/-/state/com.google/starred"
    const val TAG_ACTION_ADD: String = "a"
    const val TAG_ACTION_REMOVE: String = "r"

    val URL_USER_INFO: String = API + "/user-info"

    val URL_MARKER: String = API + "/mark-all-as-read"

    val URL_MARKER_COUNTS: String = API + "/unread-count"

    val URL_SUBSCRIPTION: String = API + "/subscription/list"

    
    val URL_SUBSCRIPTION_EDIT: String = API + "/subscription/edit"

    
    val URL_SUBSCRIPTION_QUICKADD: String = API + "/subscription/quickadd"

    //
    // public static final String URL_CATEGORIES = API+"/categories";
    //
    
    val URL_STREAM_IDS: String = API + "/stream/items/ids"

    
    val URL_STREAM_ITEMS_CONTENTS: String = API + "/stream/items/contents"


    val URL_STREAM_CONTENTS: String = API + "/stream/contents"

    //
    // public static final String URL_SEARCH_FEEDS = API+"/search/feeds";
    //
    // public static final String URL_ENTRIES_CONTENT = API+"/entries/%s";
    // public static final String URL_ENTRIES_IDS = API+"/entries/.mget";
    //
    
    val URL_TAG: String = API + "/tag/list"

    
    val URL_TAG_EDIT: String = API + "/edit-tag"

    fun isIgnoredTag(tag: String): Boolean {
        return tag.endsWith(GLOBAL_STATE_FRESH)
                || tag.endsWith(GLOBAL_STATE_BLOGGER_FOLLOWING)
                || tag.endsWith(GLOBAL_STATE_BROADCAST)
    }

    fun isIgnoredForTag(tag: String): Boolean {
        return tag.endsWith(GLOBAL_STATE_READING_LIST)
                || tag.endsWith(GLOBAL_STATE_STARRED)
    }

    // ******** InoReader ******
    const val CLIENT_ID: String = "1000001134"
    const val CLIENT_SECRET: String = "tg6UMNG9cqIJgSU7Y1zDNKrKIH5Mf0yI"
    const val HTTP_HEADER_APP_ID: String = "AppId"
    const val HTTP_HEADER_APP_KEY: String = "AppKey"

    
    val OAUTH2: String =
        "/oauth2/auth?client_id=" + CLIENT_ID + "&redirect_uri=" + Static.REDIRECT_URI + "&state=%s&scope=%s&response_type=code"
    const val TOKEN: String = "/oauth2/token"
}