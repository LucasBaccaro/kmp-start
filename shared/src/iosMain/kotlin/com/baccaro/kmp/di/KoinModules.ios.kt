package com.baccaro.kmp.di

import com.baccaro.kmp.data.IOSDatabaseDriverFactory
import com.baccaro.kmp.data.local.DataBaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule = module {
    single<DataBaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}