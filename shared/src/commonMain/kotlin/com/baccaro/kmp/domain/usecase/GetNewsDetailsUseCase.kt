package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.repository.NewsRepository
import com.baccaro.kmp.util.OperationResult

class GetNewsDetailsUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(id: Int): OperationResult<PostModel> = repository.getNewsDetails(id)
}