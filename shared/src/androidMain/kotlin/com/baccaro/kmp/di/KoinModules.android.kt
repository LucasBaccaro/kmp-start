package com.baccaro.kmp.di

import com.baccaro.kmp.data.AndroidDatabaseDriverFactory
import com.baccaro.kmp.data.local.DataBaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<DataBaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
}