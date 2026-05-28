package com.example.agroinnovexa20.domain.usecase

import com.example.agroinnovexa20.domain.repository.AuthRepository
import com.example.agroinnovexa20.core.result.Result


class LoginUseCase(
    private val repo: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<String> {

        return repo.login(email, password)
    }
}