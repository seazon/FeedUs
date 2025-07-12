package com.seazon.feedme.lib.utils

import com.seazon.feedme.platform.Crypto
import io.ktor.util.encodeBase64

object Helper {

    fun base64(s: String): String {
        return s.encodeBase64()
    }

    fun md5(s: String): String {
        val d = Crypto.md5(s)
        return if (d.length >= 24) {
            d.substring(8, 24)
        } else {
            d
        }
    }

    fun md52(s: String): String {
        return Crypto.md5(s)
    }
}