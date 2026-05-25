package com.example.agroinnovexa20.core.worker.Workmanager



import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.agroinnovexa20.core.notification.NotificationHelper
import com.example.agroinnovexa20.data.local.Room.WeatherEntity
import com.example.agroinnovexa20.data.repository.Repository
import com.example.agroinnovexa20.data.repository.WeatherCacheRepository
import com.google.firebase.auth.FirebaseAuth

class WeatherSyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val cacheRepo = WeatherCacheRepository(context)
    private val repo = Repository(FirebaseAuth.getInstance(), cacheRepo)

    override suspend fun doWork(): Result {
        Log.d("WeatherWorker", "Worker triggered! Network status: ${inputData.getString("city")}")

        return try {
            val city = inputData.getString("city") ?: return Result.failure()

            // Fetch / sync data
            repo.get_data(city)

            // Get cached weather
            val weather: WeatherEntity? = cacheRepo.getCachedWeather()
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
