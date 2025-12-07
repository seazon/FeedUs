package com.seazon.feedme.lib.rss.service.inoreader

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi

class InoreaderOldApi(token: RssToken) : InoreaderApi(token) {

    override fun getAuthType(): Int {
        return RssApi.AUTH_TYPE_BASE
    }

}
