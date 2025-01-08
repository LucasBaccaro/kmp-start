package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.util.OperationResult

class GetDetailsUseCase(private val repository: Repository) {
    suspend operator fun invoke(id:Int): OperationResult<GenericModel> = repository.getDetails(id)
}

