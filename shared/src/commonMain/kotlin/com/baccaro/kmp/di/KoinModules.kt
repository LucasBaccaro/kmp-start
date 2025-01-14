package com.baccaro.kmp.di

import PostDto
import UserDto
import com.baccaro.kmp.data.remote.ApiService
import com.baccaro.kmp.data.remote.NewsRemoteDataSource
import com.baccaro.kmp.data.remote.NewsRepository
import com.baccaro.kmp.data.remote.NewsRepositoryImpl
import com.baccaro.kmp.data.remote.UserRemoteDataSource
import com.baccaro.kmp.data.remote.UserRepository
import com.baccaro.kmp.data.remote.UserRepositoryImpl
import com.baccaro.kmp.domain.model.Coordinates
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.usecase.GetNewsDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetNewsUseCase
import com.baccaro.kmp.domain.usecase.GetUserDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetUsersUseCase
import com.baccaro.kmp.domain.usecase.SearchNewsUseCase
import com.baccaro.kmp.presentation.HomeViewModel
import com.baccaro.kmp.util.Mapper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
    // Http Client
    single {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    // API Service
    single { ApiService(get()) }

    // Remote Data Sources
    single { NewsRemoteDataSource(get()) }
    single { UserRemoteDataSource(get()) }

    // Mappers
    single<Mapper<PostDto, PostModel>> {
        object : Mapper<PostDto, PostModel> {
            override fun map(dto: PostDto) = PostModel(
                id = dto.id,
                title = dto.title,
                content = dto.content,
                thumbnail = dto.thumbnail,
                category = dto.category
            )
        }
    }

    single<Mapper<UserDto, UserModel>> {
        object : Mapper<UserDto, UserModel> {
            override fun map(dto: UserDto) = UserModel(
                id = dto.id,
                fullName = "${dto.firstName} ${dto.lastName}",
                email = dto.email,
                city = dto.address.city,
                location = Coordinates(
                    latitude = dto.address.geo.lat.toDouble(),
                    longitude = dto.address.geo.lng.toDouble()
                )
            )
        }
    }

    // Repositories
    single<NewsRepository> { NewsRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // Use Cases
    factory { GetNewsUseCase(get()) }
    factory { GetNewsDetailsUseCase(get()) }
    factory { SearchNewsUseCase(get()) }
    factory { GetUsersUseCase(get()) }
    factory { GetUserDetailsUseCase(get()) }

    // ViewModels
    viewModel { HomeViewModel(get(), get(), get()) }
}
fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(commonModule)
    }
}