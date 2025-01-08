package com.baccaro.kmp.data.repository

import GenericDto
import com.baccaro.kmp.domain.model.GenericModel
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.data.remote.RemoteDataSource
import com.baccaro.kmp.util.Mapper
import com.baccaro.kmp.util.OperationResult

class RepositoryImpl(private val remoteDataSource: RemoteDataSource, private val mapper: Mapper<GenericDto,GenericModel>) : Repository {

    override suspend fun getList(): OperationResult<List<GenericModel>> {
        return try {
            val result = remoteDataSource.getList()
            val mappedList = result.map { mapper.map(it) }
            OperationResult.Success(mappedList.filterIsInstance<GenericModel.ListModel>().flatMap { it.data })
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getDetails(id: Int): OperationResult<GenericModel> {
        return try {
            val result = remoteDataSource.getDetails(id)
            val mappedResult = mapper.map(result)
            OperationResult.Success(mappedResult)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}