package com.example.agroinnovexa20.domain.usecase



import com.example.agroinnovexa20.core.result.Result
import com.example.agroinnovexa20.domain.repository.AuthRepository

class SignupUseCase(
    private val repo: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<String> {

        return repo.signup(email, password)
    }
}