package com.baccaro.kmp.di

import GenericDto
import com.baccaro.kmp.data.remote.ApiService
import com.baccaro.kmp.data.remote.RemoteDataSource
import com.baccaro.kmp.data.repository.RepositoryImpl
import com.baccaro.kmp.domain.model.GenericModel
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
    single<Repository> { RepositoryImpl(get(),get()) }
    factory { GetListUseCase(get()) }
    factory { GetDetailsUseCase(get()) }
    factory { SearchListUseCase(get()) }
    single<Mapper<GenericDto, GenericModel>> {
        object : Mapper<GenericDto, GenericModel> {
            override fun map(dto: GenericDto): GenericModel {
                return when (dto) {
                    is GenericDto.ListDto -> {
                        GenericModel.ListModel(dto.data.map {
                            mapItem(it)
                        })
                    }
                    is GenericDto.ItemDto -> {
                        mapItem(dto)
                    }
                    else -> throw IllegalArgumentException("Unknown DTO type: $dto")
                }
            }

            fun mapItem(dto: GenericDto.ItemDto): GenericModel.ItemModel{
                return GenericModel.ItemModel(dto.id, dto.title, dto.imageUrl, dto.description)
            }
        }
    }
    single<ApiService>{ ApiService(get()) }
}

fun getCommonModules(): List<Module> = listOf(commonModule)