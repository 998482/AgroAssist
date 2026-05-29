package com.example.agroinnovexa20.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroinnovexa20.data.model.weather.Forecast
import com.example.agroinnovexa20.data.repository.WeatherCacheRepository
import com.example.agroinnovexa20.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import java.net.URLEncoder
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

    private val _statusMsg = MutableStateFlow<String?>(null)
    val statusMsg = _statusMsg.asStateFlow()

    fun fetchWeather(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value   = null

            try {
                // Query already lat,lng ya English city name hai
                // HomeViewModel ne pehle se convert kar diya
                callWeatherApi(query)
            } catch (e: Exception) {
                loadCache()
            }

            _loading.value = false
        }
    }

// resolveToEnglish function DELETE karo — ab nahi chahiye



    private suspend fun callWeatherApi(query: String) {
        val response = repo.get_data(query)
        if (response.isSuccessful && response.body() != null) {
            _weather.value = response.body()!!
        } else {
            loadCache()
        }
    }

    private suspend fun loadCache() {
        val cached = cache.getCached()
        _error.value = if (cached != null) "Offline mode (cached data)"
        else "No internet & no cache"
    }
}