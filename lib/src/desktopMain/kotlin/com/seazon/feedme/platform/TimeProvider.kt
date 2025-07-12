package com.seazon.feedme.platform

actual object TimeProvider {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}