package com.seazon.feedme.lib.rss.service

import com.seazon.feedme.lib.rss.bo.RssCategory
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinStream
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinSubscription
import com.seazon.feedme.lib.rss.service.gr.bo.GrStream
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssStream

fun GrStream.convert(): RssStream {
    return RssStream(
        continuation,
        items?.map { it.convert() } ?: ArrayList(),
        itemRefs?.map {
            convertToLongForm(it.id.orEmpty())
        }.orEmpty()
    )
}

fun TtrssStream.convert(): RssStream {
    return RssStream(
        continuation,
        content?.map { it.convert() } ?: emptyList(),
        ids.orEmpty()
    )
}

fun FeedbinStream.convert(): RssStream {
    return RssStream(
        null,
        items?.map { it.convert() } ?: ArrayList(),
        ids.orEmpty()
    )
}

//fun RssItem.convert(index: Int, includeEnclosure: Boolean): Item {
//    return Item().also {
//        it.id = id
//        it.fid = fid
//        it.title = title
//        it.link = link
//        it.author = author
//        it.publisheddate = Date(if (publisheddate == null) 0L else publisheddate!! + index)
//        it.updateddate = Date(if (updateddate == null) 0L else updateddate!! + index)
//        var enclosureString = ""
//        if (includeEnclosure && !enclosure.isNullOrEmpty()) {
//            for (e in enclosure!!) {
//                if (e.href.isNullOrEmpty()) {
//                    continue
//                }
//                if (enclosureString.contains(e.href!!)) {
//                    continue
//                }
//                // enclosure和正文url重复
//                if (description?.contains(e.href!!) == true) {
//                    continue
//                }
//                enclosureString += StringUtil.toHtml(e.href!!, e.type)
//            }
//        }
//        it.description = enclosureString + description
//        it.tags = tags
//        it.visual = visual
//        it.visualOri = visualOri
//        it.podcastUrl = podcastUrl
//        it.podcastSize = podcastSize
//        it.feed = feed?.convert()
//    }
//}
//
//fun RssFeed.convert(): Feed {
//    return Feed(
//        id ?: "",
//        title ?: "",
//        "",
//        url ?: "",
//        feedUrl ?: "",
//        if (categories.isEmpty()) "" else ",${
//            categories.joinToString(separator = ",") {
//                it.id ?: ""
//            }
//        },",
//        favicon ?: ""
//    )
//}
//
//fun RssTag.convert(): Tag {
//    return Tag().also {
//        it.id = id
//        it.title = label
//    }
//}

fun Collection<FeedbinSubscription>.convert(): List<RssFeed> {
    return map {
        RssFeed(
            it.getId(),
            it.title,
            it.site_url ?: it.feed_url,
            it.feed_url,
            it.categories?.map { category ->
                RssCategory(category.id.toString(), category.name)
            },
            null
        )
    }
}
