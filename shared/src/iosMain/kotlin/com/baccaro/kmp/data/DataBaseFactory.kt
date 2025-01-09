package com.baccaro.kmp.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.baccaro.kmp.ItemDataBase
import com.baccaro.kmp.data.local.DataBaseDriverFactory

class IOSDatabaseDriverFactory(): DataBaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            ItemDataBase.Schema,
            "item.db"
        )
    }
}