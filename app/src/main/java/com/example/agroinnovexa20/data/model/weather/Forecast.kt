package com.example.agroinnovexa20.data.model.weather


data class Forecast(
    val current: Current,
    val forecast: ForecastX,
    val location: Location
)
