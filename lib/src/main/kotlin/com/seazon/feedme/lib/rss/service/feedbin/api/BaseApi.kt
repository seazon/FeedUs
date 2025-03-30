package com.seazon.feedme.lib.rss.service.feedbin.api

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.feedbin.FeedbinConstants

open class BaseApi(val token: RssToken) {

    protected fun getSchema(): String? {
        return token.host
    }

    protected fun getExpiredTimestamp(): Long {
        return FeedbinConstants.EXPIRED_TIMESTAMP
    }
}
