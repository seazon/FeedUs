package com.seazon.feedme.lib.rss.service.inoreader

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrApi

class InoreaderOldApi(token: RssToken) : GrApi(token, SCHEMA_HTTPS, EXPIRED_TIMESTAMP) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

    companion object {
        val EXPIRED_TIMESTAMP: Long = InoreaderApi.EXPIRED_TIMESTAMP
        private const val SCHEMA_HTTPS = "https://www.inoreader.com"
    }
}
