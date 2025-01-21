package com.baccaro.kmp.di

import CategoryDetailViewModel
import CategoryRepository
import CategoryRepositoryImpl
import ClientApi
import ClientHomeViewModel
import ClientRepository
import ClientRepositoryImpl
import ClientServicesViewModel
import TokenManager
import com.baccaro.kmp.data.remote.AuthApi
import com.baccaro.kmp.data.remote.CategoryApi
import com.baccaro.kmp.data.remote.WorkerApi
import com.baccaro.kmp.data.repository.AuthRepositoryImpl
import com.baccaro.kmp.data.repository.WorkerRepositoryImpl
import com.baccaro.kmp.domain.repository.AuthRepository
import com.baccaro.kmp.domain.repository.WorkerRepository
import com.baccaro.kmp.domain.usecase.LoginUseCase
import com.baccaro.kmp.domain.usecase.RegisterUseCase
import com.baccaro.kmp.presentation.CategoriesViewModel
import com.baccaro.kmp.presentation.LoginViewModel
import com.baccaro.kmp.presentation.RegisterViewModel
import com.baccaro.kmp.presentation.WorkerHomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    single { AuthApi(get()) }
    single { CategoryApi(get(), get()) }
    single { WorkerApi(get()) }
    
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }
    single<WorkerRepository> { WorkerRepositoryImpl(get(), get()) }
    
    single { TokenManager() }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }

    single { ClientApi(get(), get()) }
    single<ClientRepository> { ClientRepositoryImpl(get()) }

    viewModelOf(::LoginViewModel)
    viewModelOf(::ClientHomeViewModel)
    viewModelOf(::ClientServicesViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::CategoriesViewModel)
    viewModelOf(::WorkerHomeViewModel)
    viewModelOf(::CategoryDetailViewModel)
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}