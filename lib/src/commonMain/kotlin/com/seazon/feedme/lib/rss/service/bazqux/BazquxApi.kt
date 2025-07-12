package com.seazon.feedme.lib.rss.service.bazqux

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrApi

class BazquxApi(token: RssToken) : GrApi(token, SCHEMA_HTTPS, EXPIRED_TIMESTAMP) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

    companion object {
        const val EXPIRED_TIMESTAMP = (2 * 24 * 3600 // 秒（2017/6/8记录）
                ).toLong()
        private const val SCHEMA_HTTPS = "https://www.bazqux.com"
    }
}
