package com.seazon.feedme.lib.rss.service.freshrss

import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrApi
import com.seazon.feedme.lib.rss.service.gr.bo.convert
import com.seazon.feedme.lib.utils.orZero

class FreshRssApi(token: RssToken) : GrApi(token, token.host, EXPIRED_TIMESTAMP) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

    override fun onHostSet(host: String): String {
        if (host.endsWith("/api/greader.php")) {
            return host
        }
        return if (host.endsWith("/")) {
            host + "api/greader.php"
        } else {
            "$host/api/greader.php"
        }
    }

    override fun getDefaultHost(): String? {
        return null
    }

    override suspend fun getUnraedStream(count: Int, since: String?, continuation: String?): RssStream? {
        return mainApi?.getContents2("user/-/state/com.google/reading-list", count, true, since, continuation)?.convert()
    }

    override fun supportCreateTag(): Boolean {
        return true
    }

    override suspend fun getTags(): List<RssTag>? {
        return mainApi?.getTags()?.tags?.filter {
            TAG_TYPE_TAG == it.type
        }?.mapNotNull {
            if (it.id.isNullOrEmpty()) {
                null
            } else {
                RssTag(it.id, it.id?.substring(it.id?.lastIndexOf("/").orZero() + 1))
            }
        }
    }

    companion object {
        const val EXPIRED_TIMESTAMP = (7 * 24 * 3600 // 秒（2023/04/30记录）
                ).toLong()
        const val TAG_TYPE_TAG = "tag"
    }
}
