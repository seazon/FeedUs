package com.seazon.feedme.lib.rss.spider

import com.seazon.feedme.lib.rss.bo.Entity

class SpiderStream : Entity() {
    var continuation: String? = null
    var items: List<SpiderItem> = ArrayList()
}
