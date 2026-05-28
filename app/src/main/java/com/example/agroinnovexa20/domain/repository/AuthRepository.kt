package com.example.agroinnovexa20.domain.repository

import com.example.agroinnovexa20.core.result.Result

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Result<String>

    suspend fun signup(
        email: String,
        password: String
    ): Result<String>
}