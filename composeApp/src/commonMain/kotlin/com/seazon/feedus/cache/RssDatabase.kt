package com.seazon.feedus.cache

import app.cash.sqldelight.db.SqlDriver
import com.seazon.feedme.lib.rss.bo.Category
import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.Item
import com.seazon.feedme.lib.rss.bo.Label

class RssDatabase(sqlDriver: SqlDriver) {
    private val database = Database(sqlDriver)

    suspend fun getFeeds(): List<Feed> {
        return database.getAllFeeds()
    }

    suspend fun getFeedById(feedId: String): Feed? {
        return database.getFeedById(feedId)
    }

    suspend fun saveFeeds(list: List<Feed>) {
        return database.clearAndCreateFeeds(list)
    }

    suspend fun updateFeedCntClientUnread(feed: Feed) {
        return database.updateFeedCntClientUnread(feed)
    }

    suspend fun clearFeeds() {
        database.clearFeeds()
    }

    suspend fun getCategories(): List<Category> {
        return database.getAllCategories()
    }

    suspend fun getCategoryById(categoryId: String): Category? {
        return database.getCategoryById(categoryId)
    }

    suspend fun saveCategories(list: List<Category>) {
        return database.clearAndCreateCategories(list)
    }

    suspend fun clearCategories() {
        database.clearCategories()
    }

    suspend fun getLabels(): List<Label> {
        return database.getAllLabels()
    }

    suspend fun getLabelById(labelId: String): Label? {
        return database.getLabelById(labelId)
    }

    suspend fun saveLabels(list: List<Label>) {
        return database.clearAndCreateLabels(list)
    }

    suspend fun getItems(): List<Item> {
        return database.getAllItems()
    }

    suspend fun saveItems(list: List<Item>) {
        return database.clearAndCreateItems(list)
    }

    suspend fun updateItemFlag(item: Item) {
        return database.updateItemFlag(item)
    }

    suspend fun updateItemStar(item: Item) {
        return database.updateItemStar(item)
    }

    suspend fun clearItems() {
        database.clearItems()
    }

}