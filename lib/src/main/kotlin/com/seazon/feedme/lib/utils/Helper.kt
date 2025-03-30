package com.seazon.feedme.lib.utils

import io.ktor.util.encodeBase64
import java.text.SimpleDateFormat
import java.util.Date

object Helper {

    fun base64(s: String): String {
        return s.encodeBase64()
    }

    fun md5(s: String): String {
        val d = s.md5()
        return if (d.length >= 24) {
            d.substring(8, 24)
        } else {
            d
        }
    }

    fun md52(s: String): String {
        return s.md5()
    }

    private val sdf_log = SimpleDateFormat("yyyyMMdd_HHmmss")

    fun formatDateLog(date: Long): String? {
        return sdf_log.format(Date(date))
    }
}