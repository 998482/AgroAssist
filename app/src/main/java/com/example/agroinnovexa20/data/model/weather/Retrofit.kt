package com.example.agroinnovexa20.data.model.weather

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val retrofit=Retrofit.Builder().
    baseUrl("https://api.weatherapi.com/v1/").
    client(OkHttpClient()).
    addConverterFactory(GsonConverterFactory.create()).build().
    create(ApiService::class.java)

}
