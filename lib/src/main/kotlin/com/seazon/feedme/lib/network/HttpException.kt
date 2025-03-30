package com.seazon.feedme.lib.network

import java.lang.Exception
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class HttpException : Exception {
    private var type: Type

    constructor(type: Type, s: String?, t: Throwable?) : super(s, t) {
        this.type = type
    }

    constructor(type: Type, t: Throwable?) : super(t) {
        this.type = type
    }

    constructor(type: Type, s: String?) : super(s) {
        this.type = type
    }

    constructor(type: Type) : super() {
        this.type = type
    }

    fun getType(): Type {
        return type
    }

    fun isUnauthorized(): Boolean {
        return type == Type.EEXPIRED
    }

    fun getHumanMessage(): String {
        when (type) {
            Type.EAUTHFAILED -> return if (message == null) {
                "Auth Failed"
            } else {
                message.orEmpty()
            }

            Type.ECONNRESET -> return "Connection reset by peer"
            Type.EASSOHOST -> return "No address associated with hostname"
            Type.ETIMEDOUT, Type.ESKTTOEX -> return "Connection timed out"
            Type.ENULLRESP -> return "Response is null"
            Type.ELOCAL -> return "Local error"
            Type.ESKTEX -> return "Socket error"
            Type.EEXPIRED -> return "Authentication error or expired"
            Type.EREMOTE, Type.ESSL -> return message.orEmpty()
            else -> return "Network error"
        }
    }

    enum class Type {
        /**
         * Auth failed
         */
        EAUTHFAILED,

        /**
         * Connection timed out
         */
        ETIMEDOUT,

        /**
         * No address associated with hostname
         */
        EASSOHOST,

        /**
         * Connection reset by peer
         */
        ECONNRESET,

        /**
         * Response is null
         */
        ENULLRESP,

        /**
         * Other SocketException
         */
        ESKTEX,

        /**
         * SSL Exception
         */
        ESSL,

        /**
         * SocketTimeoutException
         */
        ESKTTOEX,

        /**
         * Local error
         * 1. json error
         * 2. encoding error
         */
        ELOCAL,

        /**
         * expired
         */
        EEXPIRED,

        /**
         * rss service return error, need error message
         */
        EREMOTE,

        /**
         * Other error
         */
        EOTHERS
    }

    companion object {
        private const val serialVersionUID = 1L

        fun getInstance(e: Exception): HttpException {
            if (e is HttpException) {
                return e
            } else if (e is SocketException) {
                if (e.message != null && e.message!!.contains(Type.ETIMEDOUT.name)) {
                    return HttpException(Type.ETIMEDOUT, e)
                } else if (e.message != null && e.message!!.contains(Type.ECONNRESET.name)) {
                    return HttpException(Type.ECONNRESET, e)
                } else {
                    return HttpException(Type.ESKTEX, e)
                }
            } else if (e is SocketTimeoutException) {
                return HttpException(Type.ESKTTOEX, e)
            } else if (e is UnknownHostException) {
                return HttpException(Type.EASSOHOST, e)
            } else if (e is SSLException) {
                return HttpException(Type.ESSL, e)
            } else {
                return HttpException(Type.EOTHERS, e)
            }
        }
    }
}
