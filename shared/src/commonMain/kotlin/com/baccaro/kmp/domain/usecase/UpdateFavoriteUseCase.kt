package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.data.local.LocalDatabase
import com.baccaro.kmp.util.OperationResult

class UpdateFavoriteUseCase (private val localDatabase: LocalDatabase){
    suspend operator fun invoke(id: Int, isFavorite: Boolean): OperationResult<Unit> {
        return try {
            localDatabase.updateFavorite(id, isFavorite)
            OperationResult.Success(Unit)
        } catch (e: Exception){
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}