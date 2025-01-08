package com.baccaro.kmp.data.remote

import GenericDto

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getList(): List<GenericDto> = apiService.getList()
    suspend fun getDetails(id:Int): GenericDto = apiService.getDetails(id)
}