package com.seazon.feedme.lib.rss.service

//import com.seazon.feedme.ext.api.lib.bo.RssFeed
//import com.seazon.feedme.ext.api.lib.bo.RssItem
//import com.seazon.feedme.ext.api.lib.bo.RssStream
//import com.seazon.feedme.ext.api.lib.bo.RssTag
//import com.seazon.feedme.ext.api.lib.bo.RssUnreadCount
//import com.seazon.feedme.ext.api.lib.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssItem
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.bo.RssUnreadCount
import com.seazon.feedme.lib.rss.bo.RssUnreadCounts
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinItem
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinStream
import com.seazon.feedme.lib.rss.service.feedbin.bo.FeedbinSubscription
import com.seazon.feedme.lib.rss.service.gr.GrConstants
import com.seazon.feedme.lib.rss.service.gr.bo.GrItem
import com.seazon.feedme.lib.rss.service.gr.bo.GrStream
import com.seazon.feedme.lib.rss.service.gr.bo.GrSubscription
import com.seazon.feedme.lib.rss.service.gr.bo.GrUnreadCount
import com.seazon.feedme.lib.rss.service.gr.bo.GrUnreadCounts
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssItem
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssStream
import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssTag
import com.seazon.feedme.lib.utils.orZero
//import org.jetbrains.kotlin.ir.types.IdSignatureValues.continuation
//import com.seazon.feedme.rss.bo.Feed
//import com.seazon.feedme.rss.bo.Item
//import com.seazon.feedme.rss.bo.Tag
//import com.seazon.feedme.rss.feedbin.bo.FeedbinItem
//import com.seazon.feedme.rss.feedbin.bo.FeedbinStream
//import com.seazon.feedme.rss.feedbin.bo.FeedbinSubscription
//import com.seazon.feedme.rss.gr.GrConstants
//import com.seazon.feedme.rss.gr.bo.GrItem
//import com.seazon.feedme.rss.gr.bo.GrStream
//import com.seazon.feedme.rss.gr.bo.GrSubscription
//import com.seazon.feedme.rss.gr.bo.GrUnreadCount
//import com.seazon.feedme.rss.gr.bo.GrUnreadCounts
//import com.seazon.feedme.rss.ttrss.bo.TtrssItem
//import com.seazon.feedme.rss.ttrss.bo.TtrssStream
//import com.seazon.feedme.rss.ttrss.bo.TtrssTag
//import com.seazon.utils.StringUtil
//import com.seazon.utils.orZero
import java.util.Date

fun GrStream.convert(): RssStream {
    return RssStream(
        continuation,
        items?.map { it.convert() } ?: ArrayList(),
        itemRefs?.map {
            convertToLongForm(it.id.orEmpty())
        }.orEmpty()
    )
}

fun GrUnreadCounts.convert(): RssUnreadCounts {
    return RssUnreadCounts(
        max,
        unreadcounts?.map {
            (it as GrUnreadCount).run {
                val updated: Long = try {
                    newestItemTimestampUsec.orZero()
                } catch (e: Exception) {
                    0L
                }
                RssUnreadCount(id, count, updated)
            }
        }.orEmpty()
    )
}

fun TtrssStream.convert(): RssStream {
    return RssStream(
        continuation,
        content?.map { it.convert() } ?: ArrayList(),
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
                RssTag(category.id.toString(), category.name)
            },
            null
        )
    }
}

fun Collection<GrSubscription>?.convert2(): List<RssFeed> {
    return this?.map {
        RssFeed(
            it.id,
            it.title,
            it.htmlUrl ?: it.url,
            it.url,
            it.categories?.map { category ->
                RssTag(category.id, category.label)
            }?.filter {
                !GrConstants.isIgnoredTag(it.label.orEmpty()) && !GrConstants.isIgnoredForTag(it.label.orEmpty())
            },
            it.favicon.orEmpty()
        )
    }.orEmpty()
}

//fun Collection<TtrssTag>.convert3(): List<RssTag> {
//    return map {
//        RssTag(it.id, it.caption)
//    }
//}
