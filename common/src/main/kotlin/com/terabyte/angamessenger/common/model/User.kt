package com.terabyte.angamessenger.common.model

data class User(
    val id: Long,
    val nickname: String,
    val firstName: String,
    val lastName: String,
    val passwordHash: String,
)

data class UserPublic(
    val id: Long,
    val nickname: String,
)

data class UserRegisterRequest(
    val nickname: String,
    val firstName: String,
    val lastName: String,
    val password: String,
)

data class UserLoginRequest(
    val nickname: String,
    val password: String,
)

data class UserAuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
)

data class UserAuthRefreshRequest(
    val refreshToken: String,
)

data class UserAuthRefreshResponse(
    val accessToken: String,
)
