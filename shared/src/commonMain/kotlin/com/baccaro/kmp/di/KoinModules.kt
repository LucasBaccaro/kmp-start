package com.baccaro.kmp.di

import com.baccaro.kmp.data.remote.ApiService
import com.baccaro.kmp.data.remote.NewsRemoteDataSource
import com.baccaro.kmp.data.remote.NewsRepository
import com.baccaro.kmp.data.remote.NewsRepositoryImpl
import com.baccaro.kmp.data.remote.UserRemoteDataSource
import com.baccaro.kmp.data.remote.UserRepository
import com.baccaro.kmp.data.remote.UserRepositoryImpl
import com.baccaro.kmp.domain.usecase.GetNewsDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetNewsUseCase
import com.baccaro.kmp.domain.usecase.GetUserDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetUsersUseCase
import com.baccaro.kmp.domain.usecase.SearchNewsUseCase
import com.baccaro.kmp.presentation.HomeViewModel
import com.baccaro.kmp.presentation.NewsDetailViewModel
import com.baccaro.kmp.presentation.UserLocationViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val commonModule = module {
    // Http Client
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }

    // API Service
    single { ApiService(get()) }

    // Remote Data Sources
    single { NewsRemoteDataSource(get()) }
    single { UserRemoteDataSource(get()) }

    // Repositories
    single<NewsRepository> { NewsRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    // Use Cases
    factory { GetNewsUseCase(get()) }
    factory { GetNewsDetailsUseCase(get()) }
    factory { SearchNewsUseCase(get()) }
    factory { GetUsersUseCase(get()) }
    factory { GetUserDetailsUseCase(get()) }

    // ViewModels
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { NewsDetailViewModel(get()) }
    viewModel { UserLocationViewModel(get()) }
}
fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(commonModule)
    }
}