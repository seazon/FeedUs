package com.seazon.feedme.lib.rss.service.localrss

import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.localrss.bo.LocalRssCategory
import com.seazon.feedme.lib.rss.service.localrss.bo.LocalRssSubscription
import com.seazon.feedme.lib.rss.spider.BaseSpiderService
import com.seazon.feedme.lib.rss.spider.SpiderFeed
import com.seazon.feedme.lib.rss.spider.SpiderStream
import com.seazon.feedme.lib.rss.spider.basic.DefaultSpiderService
import com.seazon.feedme.lib.utils.LogUtils
//import java.rmi.RemoteException

class SpiderServiceConnection {
    var queryList: MutableList<BaseSpiderService> = ArrayList<BaseSpiderService>()
    var queryMap: MutableMap<String?, BaseSpiderService> = HashMap<String?, BaseSpiderService>()

    init {
        val s1 = DefaultSpiderService()
        queryList.add(s1)
//        queryMap.put(s1.javaClass.name, s1)
    }

    suspend fun getFeed(url: String): LocalRssSubscription? {
        try {
            var feed: LocalRssSubscription?
            var spider: SpiderFeed?
            for (key in queryMap.keys) {
                val s = queryMap.get(key)
                spider = s!!.getFeed(url)
                if (spider != null) {
                    feed = LocalRssSubscription()
                    feed.id = spider.id
                    feed.title = spider.title
                    feed.feedUrl = spider.feedUrl
                    feed.url = spider.url
                    feed.favicon = spider.favicon
                    feed.categories = ArrayList<LocalRssCategory>()
                    feed.lastClawTime = 0
                    feed.spiderPackage = key
                    return feed
                }
            }
            return null
        } catch (e: Exception) {
            LogUtils.error(e)
            return null
        }
    }

    suspend fun getItems(spiderPackage: String?, url: String?, continuation: String?): SpiderStream? {
        try {
            return queryMap.get(spiderPackage)!!.getItems(url, continuation)
        } catch (e: Exception) {
            LogUtils.error(e)
            return null
        }
    }

}
