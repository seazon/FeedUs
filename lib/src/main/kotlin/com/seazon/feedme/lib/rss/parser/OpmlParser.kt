package com.seazon.feedme.lib.rss.parser

import com.seazon.feedme.lib.utils.LoggingPlugin

class OpmlParser : LoggingPlugin {

    suspend fun parse(url: String, requester: Requester = DefaultRequester()): Opml {
        val opml = Opml()
        var category: Opml.Outline? = null
        var feed: Opml.Outline? = null
        requester.get(url).run {
            try {
                XmlParser().parse(this, object : XmlParser.ParserListener {
                    override fun onTag(path: String) {
                        if (path == "opml/body/outline") {
                            Opml.Outline().let {
                                category = it
                                opml.body.add(it)
                            }
                        }
                        if (path == "opml/body/outline/outline") {
                            Opml.Outline().let {
                                feed = it
                                category?.outlines?.add(it)
                            }
                        }
                    }

                    override fun onAttr(path: String, attrs: Map<String, String>) {
                        try {
                            when (path) {
                                "opml/head/title" -> opml.head?.title = attrs["title"]

                                "opml/body/outline" -> {
                                    category?.type = attrs["type"]
                                    category?.text = attrs["text"]
                                    category?.title = attrs["title"]
                                    category?.xmlUrl = attrs["xmlUrl"]
                                    category?.htmlUrl = attrs["htmlUrl"]
                                }

                                "opml/body/outline/outline" -> {
                                    feed?.type = attrs["type"]
                                    feed?.text = attrs["text"]
                                    feed?.title = attrs["title"]
                                    feed?.xmlUrl = attrs["xmlUrl"]
                                    feed?.htmlUrl = attrs["htmlUrl"]
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onText(path: String, text: String) {
                        try {
                            when (path) {
                                "opml/head/title" -> opml.head?.title = text
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun stop(path: String, eventType: Int): Boolean {
                        return false
                    }
                })
//            } catch (e: XmlPullParserException) {
//                throw e
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                close()
            }
        }

        return opml
    }

}