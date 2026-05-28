package com.example.agroinnovexa20.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroinnovexa20.data.model.weather.Forecast
import com.example.agroinnovexa20.data.repository.WeatherCacheRepository
import com.example.agroinnovexa20.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repo: WeatherRepository,
    private val cache: WeatherCacheRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<Forecast?>(null)
    val weather: StateFlow<Forecast?> = _weather

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = repo.get_data(city)
                if (response.isSuccessful && response.body() != null) {
                    _weather.value = response.body()!!
                } else {
                    loadCache()
                }
            } catch (e: Exception) {
                loadCache()
            }
            _loading.value = false
        }
    }

    private suspend fun loadCache() {
        val cached = cache.getCached()
        if (cached != null) {
            _error.value = "Offline mode (cached data)"
        } else {
            _error.value = "No internet & no cache"
        }
    }
}