package com.baccaro.kmp.domain.repository

import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.util.OperationResult

interface Repository {
    suspend fun getList(): OperationResult<List<ItemModel>>
    suspend fun getDetails(id:Int): OperationResult<ItemModel>
}