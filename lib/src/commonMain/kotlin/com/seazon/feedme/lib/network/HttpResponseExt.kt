package com.seazon.feedme.lib.network

import com.seazon.feedme.lib.utils.LogUtils
import com.seazon.feedme.lib.utils.toJson
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpResponse.toType(): T? {
    val s: String = this.body()
    LogUtils.debug("response: $s")
    return toJson<T>(s)
}