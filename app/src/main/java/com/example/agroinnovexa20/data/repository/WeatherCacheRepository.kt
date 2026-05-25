package com.example.agroinnovexa20.data.repository

import android.content.Context
import com.example.agroinnovexa.ui.theme.Model.Forecast
import com.example.agroinnovexa20.data.local.Room.DatabaseProvider
import com.example.agroinnovexa20.data.local.Room.WeatherEntity

class WeatherCacheRepository(context: Context) {

    private val db = DatabaseProvider.getDatabase(context)
    private val weatherDao = db.weatherDao()

    // ✅ API → Room (manual mapping happens HERE)
    suspend fun saveForecast(forecast: Forecast) {

        val entity = WeatherEntity(
            temperature = forecast.current.temp_c,
            humidity = forecast.current.humidity,
            timestamp = System.currentTimeMillis()
        )

        weatherDao.insert(entity)
    }

    // ✅ Room → advisory/UI
    suspend fun getCachedWeather(): WeatherEntity? {
        return weatherDao.getLatest()
    }
}