package com.seazon.feedus

import java.util.Locale

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val language: String = Locale.getDefault().language
}

actual fun getPlatform(): Platform = JVMPlatform()