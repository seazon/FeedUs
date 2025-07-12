package com.seazon.feedme.lib.rss.service.inoreader

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrApi

class InoreaderApi(token: RssToken) : GrApi(token, SCHEMA_HTTPS, EXPIRED_TIMESTAMP) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_OAUTH2
    }

    companion object {
        const val EXPIRED_TIMESTAMP: Long = 3600 // 秒（2017/6/8记录）
        private const val SCHEMA_HTTPS = "https://www.inoreader.com"
    }
}
