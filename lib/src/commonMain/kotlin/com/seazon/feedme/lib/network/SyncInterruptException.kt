package com.seazon.feedme.lib.network

class SyncInterruptException : Exception {
    constructor(type: Type, s: String?, t: Throwable?) : super("[" + type.name + "]" + s, t)

    constructor(type: Type, t: Throwable?) : super("[" + type.name + "]", t)

    constructor(type: Type, s: String?) : super("[" + type.name + "]" + s)

    constructor(type: Type) : super("[" + type.name + "]")

    fun getType(): Type {
        try {
            val message = message
            return Type.valueOf(message!!.substring(message.indexOf("[") + 1, message.indexOf("]")))
        } catch (e: Exception) {
            return Type.UNDEFINED
        }
    }

    fun getHumanMessage(): String {
        return when (getType()) {
            Type.JSON_PARSE -> "JSON parse error"
            Type.LOCAL_STORAGE -> "Local storage error"
            else -> "Network error"
        }
    }

    enum class Type {
        JSON_PARSE, LOCAL_STORAGE, UNDEFINED
    }

}
