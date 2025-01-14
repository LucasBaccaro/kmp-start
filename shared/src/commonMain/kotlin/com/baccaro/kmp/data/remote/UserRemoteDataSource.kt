package com.baccaro.kmp.data.remote

import com.baccaro.kmp.data.dto.UserDto

class UserRemoteDataSource(private val apiService: ApiService) {
    suspend fun getUsersList(): List<UserDto> = apiService.getUsersList()
    suspend fun getUserDetails(id: Int): UserDto = apiService.getUserDetails(id)
}