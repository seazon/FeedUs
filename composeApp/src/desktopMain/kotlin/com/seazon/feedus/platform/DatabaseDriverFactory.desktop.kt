package com.seazon.feedus.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.seazon.feedus.cache.AppDatabase
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = "jdbc:sqlite:${databaseFile.absolutePath}",
        ).also { db ->
            if (AppDatabase.Schema.version == 0L) {
                AppDatabase.Schema.create(db)
            }
        }
    }

    private val databaseFile: File
        get() = File(appDir.also { if (!it.exists()) it.mkdirs() }, "feedus.db")

    private val appDir: File
        get() {
            val os = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> {
                    File(
                        System.getenv("AppData"),
                        "feedus/db"
                    ) // "C:\Users<username>\AppData\Roaming\feedus\db"
                }

                os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                    File(
                        System.getProperty("user.home"), ".feedus"
                    ) // "/home/<username>/.feedus"
                }

                os.contains("mac") -> {
                    File(
                        System.getProperty("user.home"),
                        "Library/Application Support/feedus"
                    ) // "/Users/<username>/Library/Application Support/feedus"
                }

                else -> error("Unsupported operating system")
            }
        }
}
