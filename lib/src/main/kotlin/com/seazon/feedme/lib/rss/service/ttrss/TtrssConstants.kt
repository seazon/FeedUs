package com.seazon.feedme.lib.rss.service.ttrss

object TtrssConstants {
    const val EXPIRED_TIMESTAMP: Long = 3600 // 秒

    const val HTTP_HEADER_AUTHORIZATION_KEY: String = "Authorization"
    const val HTTP_HEADER_AUTHORIZATION_VALUE: String = "Basic %s"

    const val API: String = "/api/"

    const val METHOD_LOGIN: String = "login"
    const val METHOD_GET_COUNTERS: String = "getCounters"
    const val METHOD_GET_CATEGORIES: String = "getCategories"
    const val METHOD_GET_FEEDS: String = "getFeeds"
    const val METHOD_GET_HEADLINES: String = "getHeadlines"
    const val METHOD_UPDATE_ARTICLE: String = "updateArticle"
    const val METHOD_GET_ARTICLE: String = "getArticle"
    const val METHOD_SUBSCRIBE_TO_FEED: String = "subscribeToFeed"
    const val METHOD_UNSUBSCRIBE_FEED: String = "unsubscribeFeed"
    const val METHOD_GET_LABELS: String = "getLabels"
    const val METHOD_SET_ARTICLE_LABEL: String = "setArticleLabel"
}
