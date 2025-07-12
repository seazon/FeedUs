package com.seazon.feedme.lib.network

class SyncIgnoreException : Exception {
    constructor(s: String?, t: Throwable?) : super(s, t)

    constructor(t: Throwable?) : super(t)

    constructor(s: String?) : super(s)
}
