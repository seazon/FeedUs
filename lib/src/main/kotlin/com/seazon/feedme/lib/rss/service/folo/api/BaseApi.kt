package com.seazon.feedme.lib.rss.service.folo.api

import com.seazon.feedme.lib.rss.service.folo.FoloConstants

open class BaseApi {
    protected fun getSchema(): String {
        return FoloConstants.SCHEMA_HTTPS
    }

    protected fun getExpiredTimestamp(): Long {
        return FoloConstants.EXPIRED_TIMESTAMP
    }
}