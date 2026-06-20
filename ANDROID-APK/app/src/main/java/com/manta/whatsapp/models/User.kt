package com.manta.whatsapp.models

data class User(
    val id: String,
    val username: String,
    val role: String,
    val tier: String,
    val expired: Long
)

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val token: String, val user: User)

data class ProfileResponse(val user: User)