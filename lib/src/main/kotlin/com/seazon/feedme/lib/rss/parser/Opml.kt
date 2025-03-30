package com.seazon.feedme.lib.rss.parser

data class Opml(
    var head: Header? = null,
    var body: MutableList<Outline> = mutableListOf(),
) {
    data class Header(
        var title: String? = null,
    )

    data class Outline(
        var type: String? = null,
        var text: String? = null,
        var title: String? = null,
        var xmlUrl: String? = null,
        var htmlUrl: String? = null,
        var outlines: MutableList<Outline> = mutableListOf(),
    ){
        companion object {
            const val TYPE_RSS = "rss"
        }
    }
}