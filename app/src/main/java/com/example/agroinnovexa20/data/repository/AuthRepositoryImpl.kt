package com.example.agroinnovexa20.data.repository
import com.example.agroinnovexa20.data.remote.AuthRemoteDataSource

import com.example.agroinnovexa20.domain.repository.AuthRepository
import com.example.agroinnovexa20.core.result.Result
class AuthRepositoryImpl(
    private val remote: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<String> {

        return try {

            remote.login(email, password)

            Result.Success("Login Successful")

        } catch (e: Exception) {

            Result.Error(
                e.message ?: "Login Failed"
            )
        }
    }

    override suspend fun signup(
        email: String,
        password: String
    ): Result<String> {

        return try {

            remote.signup(email, password)

            Result.Success("Signup Successful")

        } catch (e: Exception) {

            Result.Error(
                e.message ?: "Signup Failed"
            )
        }
    }
}