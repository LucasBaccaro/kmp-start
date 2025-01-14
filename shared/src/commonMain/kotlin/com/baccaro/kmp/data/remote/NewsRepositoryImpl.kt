package com.baccaro.kmp.data.remote

import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.repository.NewsRepository
import com.baccaro.kmp.util.OperationResult
import com.baccaro.kmp.util.PostMapper

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