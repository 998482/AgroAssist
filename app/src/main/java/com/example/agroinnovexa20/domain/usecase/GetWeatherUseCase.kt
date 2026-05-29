package com.example.agroinnovexa20.domain.usecase



import com.example.agroinnovexa20.data.model.weather.Forecast
import com.example.agroinnovexa20.data.repository.WeatherRepository
import retrofit2.Response

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Response<Forecast> {
        return repository.get_data(city)
    }

}