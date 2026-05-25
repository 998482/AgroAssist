import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroinnovexa20.data.repository.Repository
import com.example.agroinnovexa20.data.repository.WeatherCacheRepository
import com.example.agroinnovexa20.data.local.Room.WeatherEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val remoteRepo: Repository,
    private val cacheRepo: WeatherCacheRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<WeatherEntity?>(null)
    val weather = _weather

    private val _loading = MutableStateFlow(false)
    val loading = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = remoteRepo.get_data(city)

                if (response.isSuccessful) {
                    val forecast = response.body()!!

                    // ✅ SAVE TO ROOM (NO CRASH)
                    cacheRepo.saveForecast(forecast)

                    _weather.value = cacheRepo.getCachedWeather()
                } else {
                    loadFromCache()
                }
            } catch (e: Exception) {
                loadFromCache()
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun loadFromCache() {
        val cached = cacheRepo.getCachedWeather()
        if (cached != null) {
            _weather.value = cached
        } else {
            _error.value = "No internet & no cached data"
        }
    }
}
