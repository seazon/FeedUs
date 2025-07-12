package com.seazon.feedme.lib.utils

import kotlin.math.min

object RssUtils {

    fun parsePublishTime(crawlTime: Long?, publishTime: Long?): Long {
        val crawlTimeMsec = when (crawlTime.toString().length) {
            13 -> crawlTime!!
            10 -> crawlTime!! * 1000
            else -> 0
        }
        val publishTimeMsec = when (publishTime.toString().length) {
            13 -> publishTime!!
            10 -> publishTime!! * 1000
            else -> 0
        }
        return if (crawlTimeMsec > 0 && publishTimeMsec > 0) {
            min(crawlTimeMsec, publishTimeMsec)
        } else if (crawlTimeMsec <= 0) {
            publishTimeMsec
        } else {
            crawlTimeMsec
        }
    }
}