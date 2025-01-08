package com.baccaro.kmp.util

interface Mapper<in DTO,out MODEL>{
    fun map(dto: DTO): MODEL
}

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T) : OperationResult<T>()
    data class Error(val exception: String) : OperationResult<Nothing>()
}