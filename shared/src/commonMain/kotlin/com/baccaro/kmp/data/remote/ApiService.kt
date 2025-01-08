package com.baccaro.kmp.data.remote

import GenericDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService(private val httpClient: HttpClient) {
    suspend fun getList(): List<GenericDto> {
        return httpClient.get("")
            .body()
    }
    suspend fun getDetails(id: Int): GenericDto {
        return httpClient.get("/$id").body()
    }
}