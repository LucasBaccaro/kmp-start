package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.data.remote.NewsRepository
import com.baccaro.kmp.data.remote.UserRepository
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.util.OperationResult

class GetNewsUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(): OperationResult<List<PostModel>> = repository.getNewsList()
}

class GetNewsDetailsUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(id: Int): OperationResult<PostModel> = repository.getNewsDetails(id)
}

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

class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): OperationResult<List<UserModel>> = repository.getUsersList()
}

class GetUserDetailsUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int): OperationResult<UserModel> = repository.getUserDetails(id)
}
