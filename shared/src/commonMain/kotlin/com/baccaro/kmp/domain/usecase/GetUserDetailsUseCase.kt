package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.repository.UserRepository
import com.baccaro.kmp.util.OperationResult

class GetUserDetailsUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int): OperationResult<UserModel> = repository.getUserDetails(id)
}