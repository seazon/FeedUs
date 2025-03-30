package com.seazon.feedme.lib.rss.spider

abstract class BaseSpiderService {

    abstract suspend fun getFeed(url: String?): SpiderFeed?
    abstract suspend fun getItems(url: String?, continuation: String?): SpiderStream?
}
