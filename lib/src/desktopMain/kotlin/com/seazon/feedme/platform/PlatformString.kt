package com.seazon.feedme.platform

actual object PlatformString {
    actual fun format(format: String, vararg args: Any?): String {
        return String.format(format, *args)
    }
}