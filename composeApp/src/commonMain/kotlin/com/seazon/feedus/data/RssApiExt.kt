package com.seazon.feedus.data

import com.seazon.feedme.lib.rss.service.RssApi

private const val FETCH_COUNT = 10
private const val FETCH_COUNT_NOT_SUPPORT_PAGINATION = 100

fun RssApi.getFetchCnt(): Int {
    return if (supportPagingFetchIds()) FETCH_COUNT else FETCH_COUNT_NOT_SUPPORT_PAGINATION
}