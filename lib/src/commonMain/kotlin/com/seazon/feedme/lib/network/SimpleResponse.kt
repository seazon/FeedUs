package com.seazon.feedme.lib.network

import com.seazon.feedme.lib.utils.toJson

data class SimpleResponse(
    val code: Int,
    val body: String,
) {
    inline fun <reified T> convertBody(): T {
        return toJson<T>(body)
    }
}