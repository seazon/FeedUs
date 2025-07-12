package com.seazon.feedme.lib.utils

//import java.nio.charset.StandardCharsets
//import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun String.base64(): String {
    return Base64.Default.encode(this.encodeToByteArray())
}

@OptIn(ExperimentalEncodingApi::class)
fun String.decodeBase64(): String {
    return Base64.Default.decode(this).decodeToString()
}

//fun String.utf8encode(): String {
//    return toByteArray(StandardCharsets.UTF_8).decodeToString()
//}