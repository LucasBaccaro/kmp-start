package com.baccaro.kmp.data.remote

import PostDto

class NewsRemoteDataSource(private val apiService: ApiService) {
    suspend fun getNewsList(): List<PostDto> = apiService.getNewsList()
    suspend fun getNewsDetails(id: Int): PostDto = apiService.getNewsDetails(id)
}