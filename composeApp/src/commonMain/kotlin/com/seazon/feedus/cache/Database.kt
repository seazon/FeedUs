package com.seazon.feedus.cache

import app.cash.sqldelight.db.SqlDriver
import com.seazon.feedme.lib.rss.bo.Category
import com.seazon.feedme.lib.rss.bo.Feed
import com.seazon.feedme.lib.rss.bo.Item

internal class Database(sqlDriver: SqlDriver) {
    private val database = AppDatabase(sqlDriver)
    private val dbQuery = database.appDatabaseQueries

    // feed
    internal fun getAllFeeds(): List<Feed> {
        return dbQuery.selectAllFeeds(::mapFeedSelecting).executeAsList()
    }

    internal fun getFeedById(feedId: String): Feed? {
        return dbQuery.selectFeedById(feedId, ::mapFeedSelecting).executeAsOneOrNull()
    }

    private fun mapFeedSelecting(
        id: String,
        title: String?,
        sortId: String?,
        url: String?,
        feedUrl: String?,
        categories: String?,
        favicon: String?,
        cntClientAll: Long,
        cntClientUnread: Long,
    ): Feed {
        return Feed(
            id = id,
            title = title,
            sortId = sortId,
            url = url,
            feedUrl = feedUrl,
            categories = categories,
            favicon = favicon,
            cntClientAll = cntClientAll.toInt(),
            cntClientUnread = cntClientUnread.toInt(),
        )
    }

    internal fun clearAndCreateFeeds(feeds: List<Feed>) {
        dbQuery.transaction {
            dbQuery.removeAllFeeds()
            feeds.forEach { feed ->
                dbQuery.insertFeed(
                    id = feed.id,
                    title = feed.title,
                    sortId = feed.sortId,
                    url = feed.url,
                    feedUrl = feed.feedUrl,
                    categories = feed.categories,
                    favicon = feed.favicon,
                    cntClientAll = feed.cntClientAll.toLong(),
                    cntClientUnread = feed.cntClientUnread.toLong(),
                )
            }
        }
    }

    internal fun updateFeedCntClientUnread(feed: Feed) {
        dbQuery.updateFeedCntClientUnread(feed.cntClientUnread.toLong(), feed.id)
    }

    fun clearFeeds() {
        dbQuery.transaction {
            dbQuery.removeAllFeeds()
        }
    }

    // category
    internal fun getAllCategories(): List<Category> {
        return dbQuery.selectAllCategories(::mapCategorySelecting).executeAsList()
    }

    internal fun getCategoryById(categoryId: String): Category? {
        return dbQuery.selectCategoryById(categoryId, ::mapCategorySelecting).executeAsOneOrNull()
    }

    private fun mapCategorySelecting(
        id: String,
        title: String?,
        sortId: String?,
        cntClientAll: Long,
        cntClientUnread: Long,
    ): Category {
        return Category(
            id = id,
            title = title,
            sortId = sortId,
            cntClientAll = cntClientAll.toInt(),
            cntClientUnread = cntClientUnread.toInt(),
        )
    }

    internal fun clearAndCreateCategories(categories: List<Category>) {
        dbQuery.transaction {
            dbQuery.removeAllCategories()
            categories.forEach { category ->
                dbQuery.insertCategory(
                    id = category.id,
                    title = category.title,
                    sortId = category.sortId,
                    cntClientAll = category.cntClientAll.toLong(),
                    cntClientUnread = category.cntClientUnread.toLong(),
                )
            }
        }
    }

    fun clearCategories() {
        dbQuery.transaction {
            dbQuery.removeAllCategories()
        }
    }

    // item
    internal fun getAllItems(): List<Item> {
        return dbQuery.selectAllItems(::mapItemSelecting).executeAsList()
    }

    private fun mapItemSelecting(
        id: String,
        fid: String?,
        flag: Long,
        status: Long,
        process: Long,
        star: Long,
        tag: Long,
        title: String?,
        titleTranslated: String?,
        link: String?,
        visual: String?,
        author: String?,
        publishedDate: Long?,
        updatedDate: Long?,
        description: String?,
        tags: String?,
    ): Item {
        return Item(
            id = id,
            fid = fid,
            flag = flag.toInt(),
            status = status.toInt(),
            process = process.toInt(),
            star = star.toInt(),
            tag = tag.toInt(),
            title = title,
            titleTranslated = titleTranslated,
            link = link,
            visual = visual,
            author = author,
            publishedDate = publishedDate,
            updatedDate = updatedDate,
            description = description,
            tags = tags,
        )
    }

    internal fun clearAndCreateItems(items: List<Item>) {
        dbQuery.transaction {
            dbQuery.removeAllItems()
            items.forEach { item ->
                dbQuery.insertItem(
                    id = item.id,
                    fid = item.fid,
                    flag = item.flag.toLong(),
                    status = item.status.toLong(),
                    process = item.process.toLong(),
                    star = item.star.toLong(),
                    tag = item.tag.toLong(),
                    title = item.title,
                    titleTranslated = item.titleTranslated,
                    link = item.link,
                    visual = item.visual,
                    author = item.author,
                    publishedDate = item.publishedDate,
                    updatedDate = item.updatedDate,
                    description = item.description,
                    tags = item.tags,
                )
            }
        }
    }

    internal fun updateItemFlag(item: Item) {
        dbQuery.updateFlag(item.flag.toLong(), item.id)
    }

    internal fun updateItemStar(item: Item) {
        dbQuery.updateStar(item.star.toLong(), item.id)
    }

    fun clearItems() {
        dbQuery.transaction {
            dbQuery.removeAllItems()
        }
    }
}