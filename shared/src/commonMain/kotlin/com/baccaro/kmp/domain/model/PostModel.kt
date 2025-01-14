package com.baccaro.kmp.domain.model

data class PostModel(
    val id: Int,
    val title: String,
    val content: String,
    val thumbnail: String,
    val category: String
)

enum class Tab {
    NEWS, USERS
}