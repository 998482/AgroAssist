package com.example.agroinnovexa20.data.local.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int = 1,
    val temperature: Double,
    val humidity: Int,
    val timestamp: Long
)
