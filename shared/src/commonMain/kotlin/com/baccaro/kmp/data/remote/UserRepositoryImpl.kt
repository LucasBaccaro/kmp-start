package com.baccaro.kmp.data.remote

import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.repository.UserRepository
import com.baccaro.kmp.util.OperationResult
import com.baccaro.kmp.util.UserMapper

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
) : UserRepository {

    override suspend fun getUsersList(): OperationResult<List<UserModel>> {
        return try {
            val result = remoteDataSource.getUsersList()
            OperationResult.Success(result.map { UserMapper().map(it) })
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getUserDetails(id: Int): OperationResult<UserModel> {
        return try {
            val result = remoteDataSource.getUserDetails(id)
            OperationResult.Success(UserMapper().map(result))
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Unknown error")
        }
    }
}