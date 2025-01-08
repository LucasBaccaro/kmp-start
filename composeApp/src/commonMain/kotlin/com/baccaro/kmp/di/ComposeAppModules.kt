package com.baccaro.kmp.di

import com.baccaro.kmp.ui.HomeViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val composeAppModule = module {
    viewModel { HomeViewModel() }
}

fun getComposeModules(): List<Module> = listOf(composeAppModule)