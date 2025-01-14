package com.baccaro.kmp.data.remote

import PostDto
import com.baccaro.kmp.data.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

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