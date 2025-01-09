package com.baccaro.kmp.data.repository

import ItemDto
import com.baccaro.kmp.data.local.LocalDatabase
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.data.remote.RemoteDataSource
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.util.Mapper
import com.baccaro.kmp.util.OperationResult

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: Mapper<ItemDto, ItemModel>,
    private val localDatabase: LocalDatabase
) : Repository {

    private var isInitialLoadComplete = false
    private var isFetching = false

    override suspend fun getList(): OperationResult<List<ItemModel>> {
        return try {
            val cachedItems = localDatabase.readAllPosts()
            if (cachedItems.isNotEmpty()) {
                return OperationResult.Success(cachedItems)
            }

            loadInitialData()
            return  OperationResult.Success(localDatabase.readAllPosts())

        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun loadInitialData() {
        if (isInitialLoadComplete || isFetching) return
        isFetching = true
        try {
            val itemsFromApi = remoteDataSource.getList()
            val mappedItems = itemsFromApi.map { mapper.map(it) }
            localDatabase.insertAllItems(mappedItems)
        } catch (e: Exception) {
            throw e
        } finally {
            isInitialLoadComplete = true
            isFetching = false
        }
    }

    override suspend fun getDetails(id: Int): OperationResult<ItemModel> {
        return OperationResult.Error("Erorrr al obtener detail")
    }

    override suspend fun clearData() {
        TODO("Not yet implemented")
    }
}