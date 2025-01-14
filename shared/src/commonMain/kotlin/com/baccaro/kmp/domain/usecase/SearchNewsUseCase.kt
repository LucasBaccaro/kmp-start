package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.repository.NewsRepository
import com.baccaro.kmp.util.OperationResult

class SearchNewsUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(query: String): OperationResult<List<PostModel>> {
        return when(val result = repository.getNewsList()) {
            is OperationResult.Success -> {
                val filteredNews = result.data.filter {
                    it.title.contains(query, true) || it.content.contains(query, true)
                }
                OperationResult.Success(filteredNews)
            }
            is OperationResult.Error -> result
        }
    }
}