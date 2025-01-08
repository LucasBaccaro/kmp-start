package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.util.OperationResult

class GetListUseCase(private val repository: Repository) {
    suspend operator fun invoke(): OperationResult<List<ItemModel>> = repository.getList()
}