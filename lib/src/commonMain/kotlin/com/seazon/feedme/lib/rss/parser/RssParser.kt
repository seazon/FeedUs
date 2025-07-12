package com.seazon.feedme.lib.rss.parser

//import java.text.SimpleDateFormat
//import java.util.*

class RssParser {

//    suspend fun parse(url: String, withItems: Boolean = true, requester: Requester = DefaultRequester()): Rss? {
//        val rss = Rss(Rss.Channel(feedId = "feed/$url"))
//        var item: Rss.Channel.Item? = null
//        requester.get(url).run {
//            try {
//                XmlParser().parse(this, object : XmlParser.ParserListener {
//                    override fun onTag(path: String) {
//                        if (path == "rss/channel/item"  // rss 2.0
//                            || path == "feed/entry"   // atom
//                        ) {
//                            Rss.Channel.Item().let {
//                                item = it
//                                rss.channel.items.add(it)
//                            }
//                        }
//                    }
//
//                    override fun onAttr(path: String, attrs: Map<String, String>) {
//                        try {
//                            when (path) {
//                                // rss 2.0
//                                "rss/channel/itunes:image" -> rss.channel.imageUrl = attrs["href"]
//                                "rss/channel/item/itunes:image" -> item?.iconUrl = attrs["href"]
//                                "rss/channel/item/enclosure" -> {
//                                    if (attrs["type"]?.startsWith("audio/") == true) {
//                                        item?.audioUrl = attrs["url"]
//                                        item?.length = attrs["length"]?.toLong()
//                                    }
//                                }
//
//                                // atom
//                                "feed/link" -> {
////                                    if (attrs["rel"] == "alternate") {
//                                    rss.channel.link = attrs["href"]
////                                    }
//                                }
//
//                                "feed/entry/link" -> item?.link = attrs["href"]
//                                "feed/entry/media:group/media:thumbnail" -> item?.iconUrl = attrs["url"]
//                                "feed/entry/media:content/media:thumbnail" -> item?.iconUrl = attrs["url"]
//                            }
//                        } catch (e: Exception) {
//                                e.printStackTrace()
//                        }
//                    }
//
//                    override fun onText(path: String, text: String) {
//                        try {
//                            when (path) {
//                                // rss 2.0
//                                "rss/channel/title" -> rss.channel.title = text
//                                "rss/channel/description" -> rss.channel.description = text
//                                "rss/channel/link" -> rss.channel.link = text
//                                "rss/channel/pubDate" -> rss.channel.pubDate = parseDate(text)
//                                "rss/channel/atom:icon" -> rss.channel.imageUrl = text
//                                "rss/channel/image/url" -> rss.channel.imageUrl = text
//
//                                "rss/channel/item/title" -> item?.title = text
//                                "rss/channel/item/link" -> item?.link = text
//                                "rss/channel/item/description" -> item?.description = text
//                                "rss/channel/item/content:encoded" -> item?.description = text
//                                "rss/channel/item/author" -> item?.author = text
//                                "rss/channel/item/pubDate" -> item?.pubDate = parseDate(text)
//                                "rss/channel/item/itunes:duration" -> item?.duration = text
//
//                                // atom
//                                "feed/title" -> rss.channel.title = text
//                                "feed/updated" -> rss.channel.pubDate = parseDate(text)
//
//                                "feed/entry/title" -> item?.title = text
//                                "feed/entry/link" -> item?.link = text
//                                "feed/entry/content" -> item?.description = text
//                                "feed/entry/author/name" -> item?.author = text
//                                "feed/entry/updated" -> item?.pubDate = parseDate(text)
//                                "feed/entry/media:group/media:description" -> item?.description = text
//                            }
//                        } catch (e: Exception) {
//                                e.printStackTrace()
//                        }
//                    }
//
//                    override fun stop(path: String, eventType: Int): Boolean {
//                        if (!withItems) {
//                             //eventType == XmlPullParser.START_TAG
//                                    //&&
//                            return      (path == "rss/channel/item" // rss 2.0
//                                    || path == "feed/entry") // atom
//                        }
//                        return false
//                    }
//                })
//            } catch (e: Exception) {
//                    e.printStackTrace()
//            } finally {
//                close()
//            }
//        }
//
//        return rss
//    }
//
//    private val sdf = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
//    private val sdf2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
//    private val sdf3 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss  Z", Locale.ENGLISH)
//    private fun parseDate(date: String?): Long {
//        date ?: return 0
//        return try {
//            sdf.parse(date.trim())?.time ?: 0
//        } catch (e: Exception) {
//            try {
//                sdf2.parse(date.trim())?.time ?: 0
//            } catch (e: Exception) {
//                try {
//                    sdf3.parse(date.trim())?.time ?: 0
//                } catch (e: Exception) {
//                        e.printStackTrace()
//                    0
//                }
//            }
//        }
//    }

}