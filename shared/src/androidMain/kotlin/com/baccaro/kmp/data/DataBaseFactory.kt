package com.baccaro.kmp.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.baccaro.kmp.ItemDB
import com.baccaro.kmp.data.local.DataBaseDriverFactory

class AndroidDatabaseDriverFactory(
    private val context: Context
): DataBaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            ItemDB.Schema,
            context,
            "item.db"
        )
    }
}