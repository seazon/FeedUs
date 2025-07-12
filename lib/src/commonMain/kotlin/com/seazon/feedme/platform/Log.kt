package com.seazon.feedme.platform

expect object Log {
    fun debug(tag: String, message: String)
    fun info(tag: String, message: String)
    fun warn(tag: String, message: String)
    fun error(tag: String, throwable: Throwable?, message: String?)
}