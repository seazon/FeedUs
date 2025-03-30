package com.seazon.feedus.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.seazon.feedus.cache.AppDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(schema = AppDatabase.Schema, name = "feedus.db")
    }
}
