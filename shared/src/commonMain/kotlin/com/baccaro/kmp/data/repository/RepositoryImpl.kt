package com.baccaro.kmp.data.repository

import ItemDto
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.data.remote.RemoteDataSource
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.util.Mapper
import com.baccaro.kmp.util.OperationResult

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: Mapper<ItemDto, ItemModel>
) : Repository {
    override suspend fun getList(): OperationResult<List<ItemModel>> {
        return try {
            val result = remoteDataSource.getList()
            OperationResult.Success(result.map { mapper.map(it) })
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getDetails(id: Int): OperationResult<ItemModel> {
        return try {
            val result = remoteDataSource.getDetails(id)
            OperationResult.Success(mapper.map(result))
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}