package com.seazon.feedme.lib.readlater

interface ReadLaterApi {
    suspend fun add(url: String, title: String? = null): String?
    suspend fun delete(id: String): String?
    suspend fun attachTags(id: String, tags: List<String>): String?
    suspend fun detachTags(id: String, tags: List<String>): String?
}