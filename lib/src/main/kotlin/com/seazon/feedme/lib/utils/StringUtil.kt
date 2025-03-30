package com.seazon.feedme.lib.utils

import java.math.BigInteger
import java.security.MessageDigest

object StringUtil {

    @JvmStatic
    fun isHttpUrl(url: String?): Boolean {
        url ?: return false
        return url.startsWith("http://") || url.startsWith("https://")
    }

//    @JvmStatic
//    fun toTwitterStyle(html: String, backgroundColor: Int, stripeColor: Int, stripeWidth: Float, gap: Float, textColor: Int): Spannable {
//        return (HtmlUtil.fromHtml(removeImage(html)) as Spannable)
//            .customizeQuoteSpans(backgroundColor, stripeColor, stripeWidth, gap)
//            .customizeURLSpans(textColor)
//    }

    @JvmStatic
    fun removeImage(text: String): String {
        return text
            .replace("<img[^>]+/>".toRegex(), "")
            .replace("<img[^>]+>[^<]*</img>".toRegex(), "")
            .replace("<img[^>]+>".toRegex(), "")
//                .replace("<a[^>]+>[^<]*</a>".toRegex(), "")
            .replace("<a class=\"img-container\"(.*?)</a>".toRegex(), "")
            .replace("<div class=\"img-wraper\"(.*?)</div>".toRegex(), "")
            .replace("<br></br>".toRegex(), "")
            .replace("<br/>".toRegex(), "")
            .replace("<br>".toRegex(), "")
    }

    @JvmStatic
    fun twitterStyle(text: String, accentColor: String, secondaryTextColor: String): String {
        val b = StringBuilder()
        b.append("<html><head>")
        b.append("<style>")
        b.append("a{color:$accentColor;text-decoration: none;}")
        // quote的样式
        b.append("blockquote{ margin: 8px 8px;border: 1px solid $secondaryTextColor;border-radius: 8px;padding: 16px;font-style: italic;}")
        b.append("</style>")
        b.append("</head>")
        b.append("<body>")
        b.append(text)
        b.append("</body>")
        b.append("</html>")
        return b.toString()
    }

    @JvmStatic
    fun toHtml(href: String, type: String?): String? {
        return when {
            type == null -> ""
            type.startsWith("audio/") -> "<div class=\"audio-wraper\"><a class=\"audio-link\" href=\"feedme://audio/$href\"><feedme class=\"icon-audio\"></feedme></a></div>"
            type.startsWith("image/") -> "<img src=\"$href\" />"
            else -> ""
        }
    }
}

//fun Spannable.customizeQuoteSpans(backgroundColor: Int, stripeColor: Int, stripeWidth: Float, gap: Float): Spannable {
//    getSpans(0, length, QuoteSpan::class.java).forEach {
//        val start = getSpanStart(it)
//        val end = getSpanEnd(it)
//        val flags = getSpanFlags(it)
//        removeSpan(it)
//        setSpan(CustomQuoteSpan(backgroundColor, stripeColor, stripeWidth, gap), start, end, flags)
//    }
//    return this
//}

//fun Spannable.customizeURLSpans(textColor: Int): Spannable {
//    getSpans(0, length, URLSpan::class.java).forEach {
//        val start = getSpanStart(it)
//        val end = getSpanEnd(it)
//        val flags = getSpanFlags(it)
//        removeSpan(it)
//        setSpan(CustomURLSpan(textColor, it.url), start, end, flags)
//    }
//    return this
//}

fun Int?.toTimeString(): String {
    return this?.run {
        val sec = this % 60
        val min = this / 60 % 60
        val hou = this / 60 / 60
        return "%d:%d:%d".format(hou, min, sec)
    } ?: run {
        return ""
    }
}

/**
 * 处理html的文本，跳过标签
 */
fun String.processHtmlText(onText: (text: String) -> String): String {
    if (isNullOrBlank()) {
        return ""
    }
    var index = 0
    var index2: Int
    val sb = StringBuilder()
    while (index < length) {
        // find start tag
        index2 = indexOf("<", index)
        if (index2 == -1) {
            // no tag anymore
            index2 = length
            sb.append(onText(substring(index, index2)))
            index = length
        } else {
            // find start tag
            // process text before index2
            if (index < index2) {
                sb.append(onText(substring(index, index2)))
            }

            index = index2
            // find end tag
            index2 = indexOf(">", index)
            if (index2 == -1) {
                throw Exception("invalid html")
            } else {
                // find end tag
                index2++
                sb.append(substring(index, index2))
                index = index2
            }
        }
    }
    return sb.toString()
}

//fun String.md5(): String {
//    val md = MessageDigest.getInstance("MD5")
//    val digest = md.digest(this.toByteArray())
//    return BigInteger(1, digest).toString(16)
//}
