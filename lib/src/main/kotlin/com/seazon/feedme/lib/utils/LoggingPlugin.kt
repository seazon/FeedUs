package com.seazon.feedme.lib.utils

interface LoggingPlugin {

    fun v(msg: Any?) {
        LogUtils.debug(this::class.java.simpleName, msg.toString())
    }

    fun d(msg: Any?) {
        LogUtils.debug(this::class.java.simpleName, msg.toString())
    }

    fun e(msg: Any?) {
        LogUtils.error(this::class.java.simpleName, msg.toString())
    }
}