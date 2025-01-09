package com.baccaro.kmp.di

import CoordDto
import ItemDto
import com.baccaro.kmp.data.local.LocalDatabase
import com.baccaro.kmp.data.remote.ApiService
import com.baccaro.kmp.data.remote.RemoteDataSource
import com.baccaro.kmp.data.repository.RepositoryImpl
import com.baccaro.kmp.domain.model.CoordModel
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.domain.usecase.GetDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetListUseCase
import com.baccaro.kmp.domain.usecase.SearchListUseCase
import com.baccaro.kmp.domain.usecase.UpdateFavoriteUseCase
import com.baccaro.kmp.presentation.HomeViewModel
import com.baccaro.kmp.util.Mapper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Text.Plain // Añade esta línea
                )
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }
    }
    single<Repository> { RepositoryImpl(get(), get(), get()) }
    single { RemoteDataSource(get()) }
    single { LocalDatabase(get()) }
    factory { GetListUseCase(get()) }
    factory { GetDetailsUseCase(get()) }
    factory { SearchListUseCase(get()) }
    factory { UpdateFavoriteUseCase(get()) }
    single<Mapper<ItemDto, ItemModel>> {
        object : Mapper<ItemDto, ItemModel> {
            override fun map(dto: ItemDto): ItemModel {
                return ItemModel(dto.country, dto.name, dto._id, mapCoord(dto.coord))
            }

            fun mapCoord(dto: CoordDto): CoordModel {
                return CoordModel(dto.lon, dto.lat)
            }
        }
    }
    single<ApiService> { ApiService(get()) }
    viewModelOf(::HomeViewModel)
}

expect val targetModule: Module

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, commonModule)
    }
}