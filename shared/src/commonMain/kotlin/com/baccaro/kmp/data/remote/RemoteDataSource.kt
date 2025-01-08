package com.baccaro.kmp.data.remote

import ItemDto

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getList(): List<ItemDto> = apiService.getList()
    suspend fun getDetails(id:Int): ItemDto = apiService.getDetails(id)
}