package com.example.agroinnovexa20.core.worker.Workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.agroinnovexa20.core.notification.NotificationHelper
import com.example.agroinnovexa20.data.local.Room.DatabaseProvider
import com.example.agroinnovexa20.data.local.Room.WeatherEntity
import com.example.agroinnovexa20.data.repository.WeatherCacheRepository
import com.example.agroinnovexa20.data.repository.WeatherRepository

class WeatherSyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    // ✅ FIX 1: Manually create using DatabaseProvider (no Hilt needed in Worker)
    private val cacheRepo = WeatherCacheRepository(context)
    private val repo = WeatherRepository(cacheRepo)

    override suspend fun doWork(): Result {
        Log.d("WeatherWorker", "Worker triggered! city: ${inputData.getString("city")}")

        return try {
            val city = inputData.getString("city") ?: return Result.failure()

            repo.get_data(city)

            // ✅ FIX 2: renamed getCachedWeather() → getCached()
            val weather: WeatherEntity? = cacheRepo.getCached()
            weather?.let {
                val notificationHelper = NotificationHelper(context)
                notificationHelper.createNotificationChannel()
                val message = generateAdvisoryMessage(it)
                notificationHelper.showNotification("Crop Advisory", message, 1)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("WeatherWorker", "Worker failed: ${e.message}")
            Result.retry()
        }
    }

    private fun generateAdvisoryMessage(weather: WeatherEntity): String {
        val risk = when {
            weather.temperature > 35 -> "HIGH"
            weather.temperature > 25 -> "MEDIUM"
            else -> "LOW"
        }
        return "Temp: ${weather.temperature}°C, Humidity: ${weather.humidity}%, Risk: $risk"
    }
}