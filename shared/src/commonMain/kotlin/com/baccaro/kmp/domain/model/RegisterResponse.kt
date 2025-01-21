package com.baccaro.kmp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val message: String
) 