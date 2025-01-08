package com.baccaro.kmp.di

import ItemDto
import com.baccaro.kmp.data.remote.ApiService
import com.baccaro.kmp.data.remote.RemoteDataSource
import com.baccaro.kmp.data.repository.RepositoryImpl
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.domain.usecase.GetDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetListUseCase
import com.baccaro.kmp.domain.usecase.SearchListUseCase
import com.baccaro.kmp.util.Mapper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }
    single { RemoteDataSource(get()) }
    single<Repository> { RepositoryImpl(get(), get()) }
    factory { GetListUseCase(get()) }
    factory { GetDetailsUseCase(get()) }
    factory { SearchListUseCase(get()) }
    single<Mapper<ItemDto, ItemModel>> {
        object : Mapper<ItemDto, ItemModel> {
            override fun map(dto: ItemDto): ItemModel {
                return ItemModel(dto.country, dto.name, dto._id)
            }
        }
    }
    single<ApiService> { ApiService(get()) }
}

fun getCommonModules(): List<Module> = listOf(commonModule)