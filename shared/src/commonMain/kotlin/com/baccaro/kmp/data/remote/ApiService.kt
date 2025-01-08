package com.baccaro.kmp.data.remote

import ItemDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService(private val httpClient: HttpClient) {
    suspend fun getList(): List<ItemDto> {
        return httpClient.get("").body()
    }

    suspend fun getDetails(id: Int): ItemDto {
        return httpClient.get("/$id").body()
    }
}