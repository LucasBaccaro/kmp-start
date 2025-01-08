package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.repository.Repository
import com.baccaro.kmp.util.OperationResult

class SearchListUseCase(private val repository: Repository){
    suspend operator fun invoke(text:String): OperationResult<List<GenericModel>> {
        return when(val result = repository.getList()){
            is OperationResult.Success -> {
                val list = result.data.filterIsInstance<GenericModel.ListModel>().flatMap { it.data }
                    .filter { it.title.contains(text, true) || it.description.contains(text,true)}
                OperationResult.Success(list)
            }
            is OperationResult.Error -> result
        }
    }
}