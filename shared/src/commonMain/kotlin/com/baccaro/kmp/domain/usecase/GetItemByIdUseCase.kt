package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.data.local.LocalDatabase
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.util.OperationResult

class GetItemByIdUseCase(private val localDatabase: LocalDatabase) {
    suspend operator fun invoke(id: Int): OperationResult<ItemModel?> {
        return try {
            val item = localDatabase.readItem(id)
            OperationResult.Success(item)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}