package com.seazon.feedme.lib.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateUtil {
    fun format(time: Long): String {
        return Instant.fromEpochMilliseconds(time).toString()
    }

    fun isoStringToTimestamp(isoDate: String?): Long? {
        return try {
            isoDate ?: return null
            Instant.parse(isoDate).toEpochMilliseconds()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * format to yyyyMMdd_HHmmss
     */
    fun formatDateLog(date: Long): String {
        val instant = Instant.fromEpochMilliseconds(date)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "%04d%02d%02d_%02d%02d%02d".format(
            localDateTime.year,
            localDateTime.monthNumber,
            localDateTime.dayOfMonth,
            localDateTime.hour,
            localDateTime.minute,
            localDateTime.second
        )
    }
}