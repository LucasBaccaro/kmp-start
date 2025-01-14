package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.repository.UserRepository
import com.baccaro.kmp.util.OperationResult

class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): OperationResult<List<UserModel>> = repository.getUsersList()
}