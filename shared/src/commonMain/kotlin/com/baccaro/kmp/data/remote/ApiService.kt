package com.baccaro.kmp.data.remote

import ItemDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.decodeBase64Bytes
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.readString
import kotlinx.serialization.json.Json

class ApiService(private val httpClient: HttpClient) {
    suspend fun getList(): List<ItemDto> {
        return httpClient.get(URL).body<String>().let { jsonString ->
            Json.decodeFromString<List<ItemDto>>(jsonString)
        }
    }

    suspend fun getDetails(id: Int): ItemDto {
        return httpClient.get("/$id").body()
    }
}

private val URL =
    "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"