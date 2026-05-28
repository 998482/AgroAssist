package com.example.agroinnovexa20.viewModel
import com.example.agroinnovexa20.data.util.GeminiRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.agroinnovexa20.data.model.advisory.CropType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
 // ← offline enum
import com.example.agroinnovexa20.domain.aimodel.CropAdvisoryResult  // ← AI model


class AiAdvisoryViewModel : ViewModel() {

    private val repository = GeminiRepository()

    private val _advisory = MutableStateFlow<CropAdvisoryResult?>(null)
    val advisory = _advisory.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _language = MutableStateFlow("hi")
    val language = _language.asStateFlow()

    fun setLanguage(lang: String) {
        _language.value = lang
    }

    fun generateAdvisory(cropName: String, temp: Double, humidity: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            _advisory.value = null
            try {
                _advisory.value = repository.getAdvisory(
                    cropName = cropName,
                    temp = temp,
                    humidity = humidity,
                    language = _language.value
                )
            } catch (e: Exception) {
                _error.value = "Salah lane mein dikkat aayi, dobara try karein"
            } finally {
                _loading.value = false
            }
        }
    }

}