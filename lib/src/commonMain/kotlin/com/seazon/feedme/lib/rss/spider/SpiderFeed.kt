package com.seazon.feedme.lib.rss.spider

import com.seazon.feedme.lib.rss.bo.Entity

class SpiderFeed : Entity() {
    var id: String? = null
    var url: String? = null
    var title: String? = null
    var feedUrl: String? = null
    var favicon: String? = null
}
