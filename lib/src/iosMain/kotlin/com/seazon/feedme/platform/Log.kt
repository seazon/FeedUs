package com.seazon.feedme.platform

actual object Log {
    actual fun debug(tag: String, message: String) {
        println("[D]/$tag: $message")
    }

    actual fun info(tag: String, message: String) {
        println("[I]/$tag: $message")
    }

    actual fun warn(tag: String, message: String) {
        println("[W]/$tag: $message")
    }

    actual fun error(tag: String, throwable: Throwable?, message: String?) {
        if (message != null) {
            println("[E]/$tag: $message")
        }
        if (throwable != null) {
            println("[E]/$tag: ${throwable.message}")
            throwable.printStackTrace()
        }
    }
}