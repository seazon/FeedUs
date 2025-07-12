package com.seazon.feedme.lib.rss.service.gr.api

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.gr.GrConfig
import com.seazon.feedme.lib.rss.service.gr.GrConstants

open class BaseApi(val token: RssToken, val config: GrConfig) {

    protected fun getSchema(): String? {
        val host = token.host
        if (host.isNullOrEmpty()) {
            return config.schemaHttps
        } else {
            return host
        }
    }

    protected fun getExpiredTimestamp(): Long {
        return config.expiredTimestamp
    }

    /**
     * InoReader
     *
     * @param headers
     */
    protected fun setHeaderAppAuthentication(headers: MutableMap<String, String>) {
        headers.put(GrConstants.HTTP_HEADER_APP_ID, GrConstants.CLIENT_ID)
        headers.put(GrConstants.HTTP_HEADER_APP_KEY, GrConstants.CLIENT_SECRET)
    }

    protected fun isTheseRssType(vararg types: String): Boolean {
        for (i in types.indices) {
            if (types[i] == token.accoutType) {
                return true
            }
        }
        return false
    }
}
