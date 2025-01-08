package com.baccaro.kmp.domain.model

data class ItemModel (
    val country: String,
    val name: String,
    val _id: Int,
    val coord: CoordModel
)

data class CoordModel (
    val lon: Double,
    val lat: Double
)