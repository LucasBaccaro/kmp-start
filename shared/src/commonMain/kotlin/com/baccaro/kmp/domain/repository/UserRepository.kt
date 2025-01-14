package com.baccaro.kmp.domain.repository

import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.util.OperationResult

interface UserRepository {
    suspend fun getUsersList(): OperationResult<List<UserModel>>
    suspend fun getUserDetails(id: Int): OperationResult<UserModel>
}