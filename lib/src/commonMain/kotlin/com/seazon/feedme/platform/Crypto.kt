package com.seazon.feedme.platform

expect object Crypto {
    fun md5(input: String): String
}