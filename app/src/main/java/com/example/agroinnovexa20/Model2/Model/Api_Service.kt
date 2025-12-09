package com.example.agroinnovexa.ui.theme.Model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Renamed according to Kotlin conventions
interface ApiService {


    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int // Dot removed here
    ): Response<Forecast>

}


