package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.GenericModel
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.util.OperationResult

class GetListUseCase(private val repository: Repository) {
    suspend operator fun invoke(): OperationResult<List<GenericModel>> = repository.getList()
}