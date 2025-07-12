package com.seazon.feedme.lib.utils

interface LoggingPlugin {

    fun v(msg: Any?) {
        LogUtils.debug(this::class.simpleName.orEmpty(), msg.toString())
    }

    fun d(msg: Any?) {
        LogUtils.debug(this::class.simpleName.orEmpty(), msg.toString())
    }

    fun e(msg: Any?) {
        LogUtils.error(this::class.simpleName.orEmpty(), msg.toString())
    }
}