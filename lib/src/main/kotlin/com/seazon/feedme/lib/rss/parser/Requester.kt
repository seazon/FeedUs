package com.seazon.feedme.lib.rss.parser

import java.io.InputStream

interface Requester {

   suspend fun get(url: String): InputStream

}