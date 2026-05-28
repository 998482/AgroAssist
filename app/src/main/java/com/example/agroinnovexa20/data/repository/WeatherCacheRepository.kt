package com.example.agroinnovexa20.data.repository

import android.content.Context
import com.example.agroinnovexa20.data.local.Room.DatabaseProvider
import com.example.agroinnovexa20.data.local.Room.WeatherEntity
import com.example.agroinnovexa20.data.model.weather.Forecast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherCacheRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dao = DatabaseProvider.getDatabase(context).weatherDao()

    suspend fun saveForecast(forecast: Forecast) {
        dao.insert(
            WeatherEntity(
                temperature = forecast.current.temp_c,
                humidity = forecast.current.humidity,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun getCached(): WeatherEntity? {
        return dao.getLatest()
    }
}