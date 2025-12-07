package com.seazon.feedme.lib.rss.service.inoreader

import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrApi
import com.seazon.feedme.lib.rss.service.gr.bo.convert

open class InoreaderApi(token: RssToken) : GrApi(token, SCHEMA_HTTPS, EXPIRED_TIMESTAMP) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_OAUTH2
    }

    override fun supportStarV2() = true

    override suspend fun getStarredStream(count: Int, continuation: String?): RssStream? {
        return mainApi?.getContents2(tagStarred, count, false, null, continuation)?.convert()
    }

    companion object {
        const val EXPIRED_TIMESTAMP: Long = 3600 // 秒（2017/6/8记录）
        private const val SCHEMA_HTTPS = "https://www.inoreader.com"
    }
}
