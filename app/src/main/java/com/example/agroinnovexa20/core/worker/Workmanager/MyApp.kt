package com.example.agroinnovexa20.core.worker.Workmanager



import android.app.Application
import androidx.work.*


import java.util.concurrent.TimeUnit

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // only run when network is ON
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<WeatherSyncWorker>(
            15, TimeUnit.MINUTES // every 1 hour
        )
            .setConstraints(constraints)
            .setInputData(workDataOf("city" to "Delhi")) // example
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "weather_sync",
            ExistingPeriodicWorkPolicy.REPLACE, // brutal: keep existing if already scheduled
            periodicWork
        )
    }
}
