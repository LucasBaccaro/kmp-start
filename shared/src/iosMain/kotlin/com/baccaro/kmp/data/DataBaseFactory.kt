package com.baccaro.kmp.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.baccaro.kmp.ItemDB
import com.baccaro.kmp.data.local.DataBaseDriverFactory

class IOSDatabaseDriverFactory(): DataBaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            ItemDB.Schema,
            "item.db"
        )
    }
}