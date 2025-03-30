package com.seazon.feedme.lib.rss.spider.basic

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpManager
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.rss.parser.Requester
import com.seazon.feedme.lib.rss.parser.RssParser
import com.seazon.feedme.lib.rss.spider.BaseSpiderService
import com.seazon.feedme.lib.rss.spider.SpiderFeed
import com.seazon.feedme.lib.rss.spider.SpiderItem
import com.seazon.feedme.lib.rss.spider.SpiderStream
import com.seazon.feedme.lib.utils.LogUtils
import java.io.InputStream

class DefaultSpiderService : BaseSpiderService() {

    override suspend fun getFeed(url: String?): SpiderFeed? {
        return try {
            RssParser().parse(url.orEmpty(), false, object : Requester {
                override suspend fun get(url: String): InputStream {
                    return HttpManager.stream(HttpMethod.GET, url)
                }
            })?.channel?.let {
                if (it.title.isNullOrEmpty()) {
                    null
                } else {
                    SpiderFeed().apply {
                        this.url = it.link
                        this.title = it.title
                        this.feedUrl = url
                        this.favicon = it.imageUrl
                    }
                }
            } ?: run {
                null
            }
        } catch (e: HttpException) {
            LogUtils.error(e)
            null
        }
    }

    override suspend fun getItems(url: String?, continuation: String?): SpiderStream? {
        LogUtils.debug("getItems, url:$url, continuation:$continuation")
        return try {
            RssParser().parse(url.orEmpty(), true, object : Requester {
                override suspend fun get(url: String): InputStream {
                    return HttpManager.stream(HttpMethod.GET, url)
                }
            })?.channel?.let {
                SpiderStream().apply {
                    this.continuation = null
                    this.items = it.items.map {
                        SpiderItem().apply {
                            this.title = it.title
                            this.url = it.link
                            this.publishTime = it.pubDate ?: System.currentTimeMillis()
                            this.content = it.description
                            this.thumbnail = it.iconUrl
                            this.author = it.author ?: ""
                        }
                    }
                }
            } ?: run {
                null
            }
        } catch (e: Exception) {
            LogUtils.error(e)
            null
        }
    }

}