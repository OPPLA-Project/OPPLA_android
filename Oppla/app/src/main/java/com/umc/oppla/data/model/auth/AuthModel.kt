package com.umc.oppla.data.model.auth

data class AuthRequest(
    val accessToken: String? = null
)

data class AuthResponse(
    val code: Int? = null,
    val isSuccess: Boolean? = null,
    val message: String? = null,
    val result: Result? = null
)

data class Result(
    val appToken: String? = null
)