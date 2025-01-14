package com.baccaro.kmp.domain.model

data class UserModel(
    val id: Int,
    val fullName: String,
    val email: String,
    val city: String,
    val location: Coordinates
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)