package com.seazon.feedme.lib.rss.service.theoldreader

import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrApi
import com.seazon.feedme.lib.rss.service.gr.bo.convert2

class TheoldreaderApi(token: RssToken) : GrApi(token, SCHEMA_HTTPS, EXPIRED_TIMESTAMP) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

    override suspend fun getSubscriptions(): List<RssFeed>? {
        return mainApi?.getSubscriptions()?.subscriptions?.apply {
            forEach {
                if (it.iconUrl != null && it.iconUrl!!.startsWith("//")) it.iconUrl = "https:${it.iconUrl}"
            }
        }.convert2()
    }

    companion object {
        const val EXPIRED_TIMESTAMP = (2 * 24 * 3600).toLong() // 秒（2017/6/8记录）
        private const val SCHEMA_HTTPS = "https://theoldreader.com"
    }
}