package com.seazon.feedme.lib.rss.parser

import com.seazon.feedme.lib.utils.format

object OpmlUtil {

    private val opmlTemplate =
        """
<?xml version="1.0" encoding="UTF-8"?>
<opml version="1.0">
    <body>
%s
    </body>
</opml>
""".trimIndent()
    private val outline1FeedTemplate =
        """
        <outline type="rss" text="%s" title="%s" xmlUrl="%s" htmlUrl="%s"/>
""".trimIndent()
    private val outline1CategoryTemplate =
        """
        <outline text="%s" title="%s">
%s
        </outline>

""".trimIndent()
    private val outline2Template =
        """
            <outline type="rss" text="%s" title="%s" xmlUrl="%s" htmlUrl="%s"/>

""".trimIndent()

    suspend fun parse(url: String): Opml {
//        return OpmlParser().parse(url, FileRequester())
        return Opml()
    }

    fun format(opml: Opml): String {
        var outlines = ""
        opml.body.forEach { o1 ->
            var outlines2 = ""
            o1.outlines.forEach { o2 ->
                outlines2 += outline2Template.format(
                    encode(o2.text),
                    encode(o2.title),
                    encode(o2.xmlUrl),
                    encode(o2.htmlUrl)
                )
            }
            outlines += if (o1.outlines.isNullOrEmpty()) {
                outline1FeedTemplate.format(
                    encode(o1.text),
                    encode(o1.title),
                    encode(o1.xmlUrl),
                    encode(o1.htmlUrl)
                )
            } else {
                outline1CategoryTemplate.format(encode(o1.text), encode(o1.title), outlines2)
            }
        }
        return opmlTemplate.format(outlines)
    }

    private fun encode(text: String?): String {
        return text.orEmpty()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace("\"", "&quot;")
    }

}