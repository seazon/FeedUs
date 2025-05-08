package com.seazon.feedme.lib.utils

object LogUtils {
    private const val TAG = "FeedMeRssApi"
    private const val LEVEL = 4 // 0 none, 1 error, 2 warn, 3 info, 4 debug

    fun isDebugMode(): Boolean {
        return LEVEL >= 4
    }

    fun debug(content: String?) {
        if (content != null && LEVEL >= 4) {
            println("$TAG\t[debug]\t$content")
        }
    }

    fun debug(tag: String = TAG, content: String?) {
        if (content != null && LEVEL >= 4) {
            println("$tag\t[debug]\t$content")
        }
    }

    fun info(content: String?) {
        if (content != null && LEVEL >= 3) {
            println("$TAG\t[info]\t$content")
        }
    }

    fun info(tag: String = TAG, content: String?) {
        if (content != null && LEVEL >= 3) {
            println("$tag\t[info]\t$content")
        }
    }

    fun warn(content: String?) {
        if (content != null && LEVEL >= 2) {
            println("$TAG\t[warn]\t$content")
        }
    }

    fun warn(tag: String = TAG, content: String?) {
        if (content != null && LEVEL >= 2) {
            println("$tag\t[warn]\t$content")
        }
    }

    fun error(e: Throwable?) {
        println("$TAG\t[error]\t${e?.message}")
        e?.printStackTrace()
    }

    fun error(content: String?) {
        println("$TAG\t[error]\t${content}")
    }

    fun error(tag: String = TAG, e: Throwable?) {
        println("$tag\t[error]\t${e?.message}")
        e?.printStackTrace()
    }

    fun error(tag: String = TAG, content: String?, e: Throwable? = null) {
        if (LEVEL >= 1) {
            println("$tag\t[error]\t$content${e?.message}")
            e?.printStackTrace()
        }
    }
}
