package com.seazon.feedme.lib.utils

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateUtil {
    fun format(time: Long): String {
        return Instant.fromEpochMilliseconds(time).toString()
    }

    fun isoStringToTimestamp(isoDate: String?): Long {
        return try {
            isoDate ?: return 0
            Instant.parse(isoDate).toEpochMilliseconds()
        } catch (e: Exception) {
            0
        }
    }
}