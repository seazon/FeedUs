package com.seazon.feedme.lib.rss.parser

import java.io.FileInputStream
import java.io.InputStream

class FileRequester() : Requester {

    override suspend fun get(url: String): InputStream {
        // TODO how to implement in kmp?
//        return context.contentResolver.openInputStream(Uri.parse(url)) ?: FileInputStream(url)
        return FileInputStream("")
    }

}