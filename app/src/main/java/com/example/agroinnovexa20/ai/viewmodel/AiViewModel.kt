package com.example.agroinnovexa20.ai.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroinnovexa20.ai.repository.GeminiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {

    private val repository =
        GeminiRepository()

    private val _response =
        MutableStateFlow("")

    val response =
        _response.asStateFlow()

    fun askAi(prompt: String) {

        viewModelScope.launch {

            val result =
                repository.askGemini(prompt)

            _response.value = result
        }
    }
}
