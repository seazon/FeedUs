package com.seazon.feedme.lib.rss.explore.general

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import io.ktor.client.call.body
import io.ktor.client.request.get
import com.seazon.feedme.lib.network.HttpManager

class GeneralRssDetector {

    suspend fun detect(url: String): String? {
        val html: String = HttpManager.client.get(url).body()
        var rssUrl: String? = null
        val handler = KsoupHtmlHandler
            .Builder()
            .onOpenTag { name, attributes, _ ->
                if (name == "link"
                    && attributes.containsKey("type")
                    && attributes["type"] == "application/rss+xml"
                ) {
                    rssUrl = attributes["href"]
                }
            }
            .build()
        val ksoupHtmlParser = KsoupHtmlParser(
            handler = handler,
        )
        try {
            ksoupHtmlParser.write(html)
            return rssUrl
        } finally {
            ksoupHtmlParser.end()
        }
    }

}