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

            // Si la bd esta vacia, carga de la API
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
        // Primero intentar obtener de la base de datos
        val cachedItem = localDatabase.readItem(id)
        if (cachedItem != null) {
            return OperationResult.Success(cachedItem)
        }

        // Si no está en la base de datos, obtener de la API y guardar
        return try {
            val result = remoteDataSource.getDetails(id)
            val mappedItem = mapper.map(result)
            localDatabase.insertItem(mappedItem)
            OperationResult.Success(mappedItem)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun clearData() {
        localDatabase.clearAllData()
        isInitialLoadComplete = false
    }
}