package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.util.OperationResult

class GetDetailsUseCase(private val repository: Repository) {
    suspend operator fun invoke(id:Int): OperationResult<ItemModel> = repository.getDetails(id)
}

