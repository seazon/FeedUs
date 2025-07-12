package com.seazon.feedme.platform

expect object TimeProvider {
    fun currentTimeMillis(): Long
}