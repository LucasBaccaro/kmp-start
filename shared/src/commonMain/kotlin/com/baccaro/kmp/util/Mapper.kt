package com.baccaro.kmp.util

import PostDto
import UserDto
import com.baccaro.kmp.domain.model.Coordinates
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.UserModel

interface Mapper<in DTO, out MODEL> {
    fun map(dto: DTO): MODEL
}

class PostMapper : Mapper<PostDto, PostModel> {
    override fun map(dto: PostDto) = PostModel(
        id = dto.id,
        title = dto.title,
        content = dto.content,
        thumbnail = dto.thumbnail,
        category = dto.category
    )
}

class UserMapper : Mapper<UserDto, UserModel> {
    override fun map(dto: UserDto) = UserModel(
        id = dto.id,
        fullName = "${dto.firstName} ${dto.lastName}",
        email = dto.email,
        city = dto.address.city,
        location = Coordinates(dto.address.geo.lat.toDouble(), dto.address.geo.lng.toDouble())
    )
}

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T) : OperationResult<T>()
    data class Error(val exception: String) : OperationResult<Nothing>()
}