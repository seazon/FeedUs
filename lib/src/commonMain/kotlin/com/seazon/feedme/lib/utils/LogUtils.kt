package com.seazon.feedme.lib.utils

import com.seazon.feedme.platform.Log

object LogUtils {
    private const val TAG = "FeedMeRssApi"
    private const val LEVEL = 4 // 0 none, 1 error, 2 warn, 3 info, 4 debug

    fun isDebugMode(): Boolean {
        return LEVEL >= 4
    }

    fun debug(content: String?) {
        if (content != null && LEVEL >= 4) {
            Log.debug(TAG, content)
        }
    }

    fun debug(tag: String = TAG, content: String?) {
        if (content != null && LEVEL >= 4) {
            Log.debug(tag, content)
        }
    }

    fun info(content: String?) {
        if (content != null && LEVEL >= 3) {
            Log.info(TAG, content)
        }
    }

    fun info(tag: String = TAG, content: String?) {
        if (content != null && LEVEL >= 3) {
            Log.info(tag, content)
        }
    }

    fun warn(content: String?) {
        if (content != null && LEVEL >= 2) {
            Log.warn(TAG, content)
        }
    }

    fun warn(tag: String = TAG, content: String?) {
        if (content != null && LEVEL >= 2) {
            Log.warn(tag, content)
        }
    }

    fun error(e: Throwable?) {
        if (LEVEL >= 1) {
            Log.error(TAG, e, null)
            e?.printStackTrace()
        }
    }

    fun error(content: String?) {
        if (LEVEL >= 1) {
            Log.error(TAG, null, content)
        }
    }

    fun error(tag: String = TAG, e: Throwable?) {
        if (LEVEL >= 1) {
            Log.error(tag, e, null)
            e?.printStackTrace()
        }
    }

    fun error(tag: String = TAG, content: String?, e: Throwable? = null) {
        if (LEVEL >= 1) {
            Log.error(tag, e, content)
            e?.printStackTrace()
        }
    }
}
