package com.seazon.feedme.lib.rss.service.gr

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi

class GReaderApi(token: RssToken) : GrApi(token, token.host, EXPIRED_TIMESTAMP) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

    companion object {
        const val EXPIRED_TIMESTAMP = (1 * 24 * 3600).toLong()
    }
}