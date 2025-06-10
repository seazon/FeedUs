package com.seazon.feedus

import androidx.compose.runtime.Composable
import feedus.composeapp.generated.resources.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

object DateUtil {

    @Composable
    fun toXAgo(time2: Long): String {
//        try {
        var now = Clock.System.now().toEpochMilliseconds()
        var deltaSec = (now - time2) / 1000
        if (deltaSec < 60) {
            return stringResource(resource = Res.string.item_time_just_now)
        }
        var deltaMin = deltaSec / 60
        if (deltaMin < 2) {
            return stringResource(resource = Res.string.item_time_1_minute_ago)
        }
        if (deltaMin < 60) {
            return "${deltaMin.toInt()}${stringResource(resource = Res.string.item_time_x_minutes_ago)}"
        }
        var deltaHour = deltaMin / 60
        if (deltaHour < 2) {
            return stringResource(resource = Res.string.item_time_1_hour_ago)
        }
        if (deltaHour < 24) {
            return "${deltaHour.toInt()}${stringResource(resource = Res.string.item_time_x_hours_ago)}"
        }
        var deltaDay = deltaHour / 24
        if (deltaDay < 2) {
            return stringResource(resource = Res.string.item_time_1_day_ago)
        }
        if (deltaDay < 7) {
            return "${deltaDay.toInt()}${stringResource(resource = Res.string.item_time_x_days_ago)}"
        }
        val d = Instant.fromEpochMilliseconds(time2).toLocalDateTime(TimeZone.currentSystemDefault())
        if (d.year == Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year) {
            return LocalDateTime.Format { byUnicodePattern("MM/dd") }.format(d)
        }
        return LocalDateTime.Format { byUnicodePattern("yyyy/MM/dd") }.format(d)
//        } catch (e: Exception) {
////            e.printError()
//            return time2.toString()
//        }
    }
}