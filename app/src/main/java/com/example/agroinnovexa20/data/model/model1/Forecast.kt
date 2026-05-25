package com.example.agroinnovexa.ui.theme.Model

data class Forecast(
    val current: Current,
    val forecast: ForecastX,
    val location: Location
)
//https://api.weatherapi.com/v1/forecast.json?key=d21e8af2992b400186322406252409&q=India&days=6
//d21e8af2992b400186322406252409
//http://api.weatherapi.com/v1