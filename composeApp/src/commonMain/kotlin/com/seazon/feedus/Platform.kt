package com.seazon.feedus

interface Platform {
    val name: String
    val language: String
}

expect fun getPlatform(): Platform