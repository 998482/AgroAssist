package com.example.agroinnovexa20.data.repository

import com.example.agroinnovexa20.data.model.weather.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val cacheRepo: WeatherCacheRepository
) {
    suspend fun get_data(city: String): Response<Forecast> {
        return try {
            val response = RetrofitClient.retrofit.getForecast(
                apikey.apikey,
                city,
                6
            )
            if (response.isSuccessful && response.body() != null) {
                cacheRepo.saveForecast(response.body()!!)
            }
            response
        } catch (e: Exception) {
            val cached = cacheRepo.getCached()
            if (cached == null) {
                return Response.error(500, okhttp3.ResponseBody.create(null, "No cache"))
            }
            val offlineForecast = Forecast(
                location = Location(
                    name = city, country = "Offline", lat = 0.0, lon = 0.0,
                    region = "", tz_id = "", localtime = "Offline", localtime_epoch = 0
                ),
                current = Current(
                    cloud = 0, condition = Condition(0, "", ""),
                    dewpoint_c = 0.0, dewpoint_f = 0.0, diff_rad = 0.0, dni = 0.0,
                    feelslike_c = 0.0, feelslike_f = 0.0, gti = 0.0, gust_kph = 0.0,
                    gust_mph = 0.0, heatindex_c = 0.0, heatindex_f = 0.0,
                    humidity = cached.humidity, is_day = 1, last_updated = "Offline",
                    last_updated_epoch = 0, precip_in = 0.0, precip_mm = 0.0,
                    pressure_in = 0.0, pressure_mb = 0.0, short_rad = 0.0,
                    temp_c = cached.temperature, temp_f = 0.0, uv = 0.0,
                    vis_km = 0.0, vis_miles = 0.0, wind_degree = 0, wind_dir = "",
                    wind_kph = 0.0, wind_mph = 0.0, windchill_c = 0.0, windchill_f = 0.0
                ),
                forecast = ForecastX(emptyList())
            )
            Response.success(offlineForecast)
        }
    }
}
object apikey{
    val apikey="d21e8af2992b400186322406252409"
}