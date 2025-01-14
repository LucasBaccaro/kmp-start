package com.baccaro.kmp.domain.repository

import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.util.OperationResult

interface NewsRepository {
    suspend fun getNewsList(): OperationResult<List<PostModel>>
    suspend fun getNewsDetails(id: Int): OperationResult<PostModel>
}