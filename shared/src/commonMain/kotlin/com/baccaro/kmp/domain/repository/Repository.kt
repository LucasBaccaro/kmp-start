package com.baccaro.kmp.domain.repository

import com.baccaro.kmp.domain.model.GenericModel
import com.baccaro.kmp.util.OperationResult

interface Repository {
    suspend fun getList(): OperationResult<List<GenericModel>>
    suspend fun getDetails(id:Int): OperationResult<GenericModel>
}