package com.seazon.feedme.lib.rss.parser

data class Rss(val channel: Channel)  {

    public data class Channel(
        var title: String? = null,
        var feedId: String? = null,
        var link: String? = null,
        var description: String? = null,
        var pubDate: Long? = null,
        var imageUrl: String? = null,
        var items: MutableList<Item> = mutableListOf()
    )   {

        data class Item(
            var title: String? = null,
            var link: String? = null,
            var description: String? = null,
            var author: String? = null,
            var pubDate: Long? = null,
            var duration: String? = null,
            var audioUrl: String? = null,
            var length: Long? = null,
            var iconUrl: String? = null
        )
    }
}