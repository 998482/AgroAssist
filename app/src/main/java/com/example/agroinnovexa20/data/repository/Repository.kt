package com.example.agroinnovexa20.data.repository

import com.example.agroinnovexa.ui.theme.Model.Condition
import com.example.agroinnovexa.ui.theme.Model.Current
import com.example.agroinnovexa.ui.theme.Model.Forecast
import com.example.agroinnovexa.ui.theme.Model.ForecastX
import com.example.agroinnovexa.ui.theme.Model.Location
import com.example.agroinnovexa.ui.theme.Model.RetrofitClient
import com.example.agroinnovexa20.data.repository.WeatherCacheRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import retrofit2.Response

sealed class Result<out T>{
    data object Idle:Result<Nothing>()
    data object Loading:Result<Nothing>()
    data class Success<T>(val message:T):Result<T>()
    data class Error(val message : String):Result<Nothing>()

}

class Repository(val firebaseAuth: FirebaseAuth,
                 private val cacheRepo: WeatherCacheRepository
){
    suspend fun login(email:String, password:String):Result<String>{
       return try{
           firebaseAuth.signInWithEmailAndPassword(email,password).await()
           Result.Success("Login Successful")
       }
       catch(e: Exception){
           Result.Error(e.message?:"Login Failed")
       }

    }
    suspend fun signIn(email:String, password:String):Result<String>{
        return try{
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            Result.Success("SignUp Successful")
        }
        catch(e:Exception){
            Result.Error(e.message?:"SignUp Failed")
        }
    }
    suspend fun get_data(city:String): Response<Forecast>
    {
        return try {
            // 🌐 API CALL
            val response =
                RetrofitClient.retrofit.getForecast(apikey.apikey, city, 6)

            if (response.isSuccessful && response.body() != null) {
                // 💾 SAVE TO ROOM
                cacheRepo.saveForecast(response.body()!!)
            }

            response
        } catch (e: Exception) {

            // 🔁 API FAILED → ROOM FALLBACK
            val cached = cacheRepo.getCachedWeather()
                ?: throw e   // no cache → real failure

            // 🔁 Room → Forecast (minimal fields only)
            val offlineForecast = Forecast(
                location = Location(
                    name = city,
                    country = "Offline",
                    lat = 0.0,
                    lon = 0.0,
                    region = "",
                    tz_id = "",
                    localtime = "Offline",
                    localtime_epoch = (System.currentTimeMillis() / 1000).toInt()
                ),
                current = Current(
                    cloud = 0,
                    condition = Condition(0, "", ""),
                    dewpoint_c = 0.0,
                    dewpoint_f = 0.0,
                    diff_rad = 0.0,
                    dni = 0.0,
                    feelslike_c = 0.0,
                    feelslike_f = 0.0,
                    gti = 0.0,
                    gust_kph = 0.0,
                    gust_mph = 0.0,
                    heatindex_c = 0.0,
                    heatindex_f = 0.0,
                    humidity = cached.humidity,
                    is_day = 1,
                    last_updated = "Offline",
                    last_updated_epoch = 0,
                    precip_in = 0.0,
                    precip_mm = 0.0,
                    pressure_in = 0.0,
                    pressure_mb = 0.0,
                    short_rad = 0.0,
                    temp_c = cached.temperature,
                    temp_f = 0.0,
                    uv = 0.0,
                    vis_km = 0.0,
                    vis_miles = 0.0,
                    wind_degree = 0,
                    wind_dir = "",
                    wind_kph = 0.0,
                    wind_mph = 0.0,
                    windchill_c = 0.0,
                    windchill_f = 0.0
                ),
                forecast = ForecastX(
                    forecastday = emptyList()
                )
            )

            Response.success(offlineForecast)
        }
    }
}

object apikey{
    val apikey="d21e8af2992b400186322406252409"
}