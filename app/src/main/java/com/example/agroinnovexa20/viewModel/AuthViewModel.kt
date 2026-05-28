package com.example.agroinnovexa20.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroinnovexa20.core.result.Result
import com.example.agroinnovexa20.data.remote.AuthRemoteDataSource
import com.example.agroinnovexa20.data.repository.AuthRepositoryImpl
import com.example.agroinnovexa20.domain.usecase.LoginUseCase
import com.example.agroinnovexa20.domain.usecase.SignupUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    // FIREBASE
    private val firebaseAuth =
        FirebaseAuth.getInstance()

    // REMOTE DATA SOURCE
    private val remote =
        AuthRemoteDataSource(firebaseAuth)

    // REPOSITORY
    private val repository =
        AuthRepositoryImpl(remote)

    // USE CASES
    private val loginUseCase =
        LoginUseCase(repository)

    private val signupUseCase =
        SignupUseCase(repository)

    // AUTH STATE
    private val _authState =
        MutableStateFlow<Result<String>>(Result.Idle)

    val authState =
        _authState.asStateFlow()

    // AUTO LOGIN CHECK
    fun isUserLoggedIn(): Boolean {

        return firebaseAuth.currentUser != null
    }

    // LOGIN
    fun login(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            // EMAIL EMPTY
            if (email.isBlank()) {

                _authState.value =
                    Result.Error(
                        "Email required"
                    )

                return@launch
            }

            // EMAIL FORMAT
            if (
                !Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()
            ) {

                _authState.value =
                    Result.Error(
                        "Invalid email format"
                    )

                return@launch
            }

            // PASSWORD EMPTY
            if (password.isBlank()) {

                _authState.value =
                    Result.Error(
                        "Password required"
                    )

                return@launch
            }

            // PASSWORD LENGTH
            if (password.length < 6) {

                _authState.value =
                    Result.Error(
                        "Password must be at least 6 characters"
                    )

                return@launch
            }

            // LOADING
            _authState.value =
                Result.Loading

            // LOGIN
            _authState.value =
                loginUseCase(
                    email,
                    password
                )
        }
    }

    // SIGNUP
    fun signUp(
        email: String,
        password: String,
        confirmPassword: String
    ) {

        viewModelScope.launch {

            // EMAIL EMPTY
            if (email.isBlank()) {

                _authState.value =
                    Result.Error(
                        "Email required"
                    )

                return@launch
            }

            // EMAIL FORMAT
            if (
                !Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()
            ) {

                _authState.value =
                    Result.Error(
                        "Invalid email format"
                    )

                return@launch
            }

            // PASSWORD EMPTY
            if (password.isBlank()) {

                _authState.value =
                    Result.Error(
                        "Password required"
                    )

                return@launch
            }

            // PASSWORD LENGTH
            if (password.length < 6) {

                _authState.value =
                    Result.Error(
                        "Password must be at least 6 characters"
                    )

                return@launch
            }

            // CONFIRM PASSWORD
            if (password != confirmPassword) {

                _authState.value =
                    Result.Error(
                        "Passwords do not match"
                    )

                return@launch
            }

            // LOADING
            _authState.value =
                Result.Loading

            // SIGNUP
            _authState.value =
                signupUseCase(
                    email,
                    password
                )
        }
    }

    // LOGOUT
    fun logout() {

        firebaseAuth.signOut()

        _authState.value =
            Result.Idle
    }

    // RESET STATE
    fun resetState() {

        _authState.value =
            Result.Idle
    }
    // CURRENT USER EMAIL
    fun getCurrentUserEmail(): String? {
        return firebaseAuth.currentUser?.email
    }
    fun changePassword(newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                firebaseAuth.currentUser!!.updatePassword(newPassword).await()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error")
            }
        }
    }
}