package com.seazon.feedme.lib.rss.service.feedly.api

import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants

open class BaseApi {
    protected fun getSchema(): String {
        return FeedlyConstants.SCHEMA_HTTPS
    }

    protected fun getExpiredTimestamp(): Long {
        return FeedlyConstants.EXPIRED_TIMESTAMP
    }
}
