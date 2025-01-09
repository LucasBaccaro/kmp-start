package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.data.local.LocalDatabase
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.util.OperationResult

class SearchListUseCase(
    private val localDatabase: LocalDatabase,
) {
    suspend operator fun invoke(
        text: String,
        onlyFavorites: Boolean = false
    ): OperationResult<List<ItemModel>> {
        return try {
            val resultFromDb = localDatabase.searchItems(text, onlyFavorites)
            OperationResult.Success(resultFromDb)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}