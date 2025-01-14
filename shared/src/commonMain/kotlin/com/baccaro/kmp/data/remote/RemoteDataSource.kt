package com.baccaro.kmp.data.remote

import PostDto
import UserDto
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.util.OperationResult
import com.baccaro.kmp.util.PostMapper
import com.baccaro.kmp.util.UserMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get


// Repository Interfaces
interface NewsRepository {
    suspend fun getNewsList(): OperationResult<List<PostModel>>
    suspend fun getNewsDetails(id: Int): OperationResult<PostModel>
}

interface UserRepository {
    suspend fun getUsersList(): OperationResult<List<UserModel>>
    suspend fun getUserDetails(id: Int): OperationResult<UserModel>
}

// Repository Implementations
class NewsRepositoryImpl(
    private val remoteDataSource: NewsRemoteDataSource,
) : NewsRepository {

    override suspend fun getNewsList(): OperationResult<List<PostModel>> {
        return try {
            val result = remoteDataSource.getNewsList()
            OperationResult.Success(result.map { PostMapper().map(it) })
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getNewsDetails(id: Int): OperationResult<PostModel> {
        return try {
            val result = remoteDataSource.getNewsDetails(id)
            OperationResult.Success(PostMapper().map(result))
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
) : UserRepository {

    override suspend fun getUsersList(): OperationResult<List<UserModel>> {
        return try {
            val result = remoteDataSource.getUsersList()
            OperationResult.Success(result.map { UserMapper().map(it) })
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getUserDetails(id: Int): OperationResult<UserModel> {
        return try {
            val result = remoteDataSource.getUserDetails(id)
            OperationResult.Success(UserMapper().map(result))
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}

// Remote Data Sources
class NewsRemoteDataSource(private val apiService: ApiService) {
    suspend fun getNewsList(): List<PostDto> = apiService.getNewsList()
    suspend fun getNewsDetails(id: Int): PostDto = apiService.getNewsDetails(id)
}

class UserRemoteDataSource(private val apiService: ApiService) {
    suspend fun getUsersList(): List<UserDto> = apiService.getUsersList()
    suspend fun getUserDetails(id: Int): UserDto = apiService.getUserDetails(id)
}

// API Service

class ApiService(private val httpClient: HttpClient) {
    suspend fun getNewsList(): List<PostDto> {
        return httpClient.get("https://jsonplaceholder.org/posts").body()
    }

    suspend fun getNewsDetails(id: Int): PostDto {
        return httpClient.get("https://jsonplaceholder.org/posts/$id").body()
    }

    suspend fun getUsersList(): List<UserDto> {
        return httpClient.get("https://jsonplaceholder.org/users").body()
    }

    suspend fun getUserDetails(id: Int): UserDto {
        return httpClient.get("https://jsonplaceholder.org/users/$id").body()
    }
}