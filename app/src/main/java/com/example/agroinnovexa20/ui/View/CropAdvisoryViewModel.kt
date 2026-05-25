package com.example.agroinnovexa20.ui.View

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.agroinnovexa20.data.model.model3.CropAdvisoryResult
import com.example.agroinnovexa20.data.model.model3.CropType
import com.example.agroinnovexa20.data.model.model3.getCropAdvisory

class CropAdvisoryViewModel : ViewModel() {

    private val _advisory = mutableStateOf<CropAdvisoryResult?>(null)
    val advisory = _advisory

    fun generateAdvisory(
        crop: CropType,
        temp: Double,
        humidity: Int
    ) {
        _advisory.value = getCropAdvisory(
            crop = crop,
            temp = temp,
            humidity = humidity
        )
    }
}