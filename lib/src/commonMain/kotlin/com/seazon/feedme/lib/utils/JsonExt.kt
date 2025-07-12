package com.seazon.feedme.lib.utils

import com.seazon.feedme.lib.rss.service.Static
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.KType

//import kotlin.reflect.full.createType

inline fun <reified T> toJson(json: String): T {
    return Static.defaultJson.decodeFromString<T>(json)
}

fun jsonOf(vararg pairs: Pair<String, Any?> = emptyArray()): JsonElement {
    return mapOf(*pairs).toJsonElement()
}

fun jsonOf(array: Array<String>): String {
    return Static.defaultJson.encodeToString(array)
}

fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is JsonElement -> this
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Array<*> -> JsonArray(map { it.toJsonElement() })
    is List<*> -> JsonArray(map { it.toJsonElement() })
    is Map<*, *> -> JsonObject(map { it.key.toString() to it.value.toJsonElement() }.toMap())
//    else -> Json.encodeToJsonElement(serializer(this::class.createType()), this)
    else -> Json.encodeToJsonElement(serializer(this::class as KType), this)
}