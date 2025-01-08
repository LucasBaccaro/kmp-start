package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.util.OperationResult

class SearchListUseCase(private val repository: Repository){
    suspend operator fun invoke(text:String): OperationResult<List<ItemModel>> {
        return when(val result = repository.getList()){
            is OperationResult.Success -> {
                val list = result.data.filter { it.name.contains(text, true) || it.country.contains(text,true)}
                OperationResult.Success(list)
            }
            is OperationResult.Error -> result
        }
    }
}