package com.seazon.feedme.lib.rss.spider

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.service.Static

class SpiderItem : Entity() {
    var title: String? = null
    var url: String? = null
    var publishTime: Long = 0
    var content: String? = null
    var thumbnail: String? = null
    var author: String? = null

    companion object {
        fun parseList(json: String?): List<SpiderItem> {
            return Static.defaultJson.decodeFromString<List<SpiderItem>>(json.orEmpty())
        }
    }
}
