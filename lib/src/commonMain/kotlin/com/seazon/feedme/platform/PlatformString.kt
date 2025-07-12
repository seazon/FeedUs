package com.seazon.feedme.platform

expect object PlatformString {
    fun format(format: String, vararg args: Any?): String
}