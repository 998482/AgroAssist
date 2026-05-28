package com.example.agroinnovexa20.viewModel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class CropAdviceViewModel(
    application: Application
) : AndroidViewModel(application) {

    // LOADING STATE
    private val _loading =
        mutableStateOf(false)

    val loading: State<Boolean> =
        _loading

    // ERROR STATE
    private val _error =
        mutableStateOf<String?>(null)

    val error: State<String?> =
        _error

    // RULE BASED FARMING
    fun fetchRuleBasedWeather(
        location: String,
        crop: String
    ) {

        try {

            _loading.value = true

            val result =
                when (crop.trim().lowercase()) {

                    "wheat" -> {
                        "Rule Based: Wheat grows well in cool climate."
                    }

                    "rice" -> {
                        "Rule Based: Rice needs high water and humidity."
                    }

                    "tomato" -> {
                        "Rule Based: Tomato prefers moderate temperature."
                    }

                    else -> {
                        "Rule Based: No rule found for this crop."
                    }
                }

            _error.value = result

        } catch (e: Exception) {

            _error.value = e.message

        } finally {

            _loading.value = false
        }
    }
}