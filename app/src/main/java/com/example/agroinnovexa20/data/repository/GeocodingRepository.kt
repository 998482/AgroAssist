package com.example.agroinnovexa20.data.repository


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

sealed class GeoResult {
    data class Success(val englishCityName: String, val lat: Double, val lng: Double) : GeoResult()
    data class Error(val message: String) : GeoResult()
}

@Singleton
class GeocodingRepository @Inject constructor() {

    /**
     * Hindi ya koi bhi language ka city name lo
     * Nominatim se English name + coordinates wapas aata hai
     * Phir wahi English name WeatherAPI mein dete hain
     */
    suspend fun resolveCity(cityInput: String): GeoResult =
        withContext(Dispatchers.IO) {
            try {
                if (cityInput.isBlank())
                    return@withContext GeoResult.Error("City name empty hai")

                val encoded = URLEncoder.encode(cityInput.trim(), "UTF-8")

                // accept-language=en → English display_name milega
                // countrycodes=in   → India ke results prefer karega
                val url =
                    "https://nominatim.openstreetmap.org/search" +
                            "?q=$encoded" +
                            "&format=json" +
                            "&limit=1" +
                            "&accept-language=en" +
                            "&countrycodes=in"

                val conn = URL(url).openConnection()
                conn.setRequestProperty(
                    "User-Agent",
                    "AgroInnovexaApp/2.0 (agroinnovexa@example.com)"
                )
                conn.connectTimeout = 8_000
                conn.readTimeout    = 8_000

                val body = conn.getInputStream().bufferedReader().readText()
                val arr  = JSONArray(body)

                if (arr.length() == 0)
                    return@withContext GeoResult.Error(
                        "\"$cityInput\" nahi mila. Doosra naam try karo."
                    )

                val obj  = arr.getJSONObject(0)
                val lat  = obj.getString("lat").toDouble()
                val lng  = obj.getString("lon").toDouble()

                // display_name = "Lucknow, Lucknow District, Uttar Pradesh, India"
                // Sirf pehla word (city name) lena hai WeatherAPI ke liye
                val fullName   = obj.getString("display_name")
                val cityName   = fullName.split(",").first().trim()   // "Lucknow"

                GeoResult.Success(cityName, lat, lng)

            } catch (e: Exception) {
                GeoResult.Error("Network error: ${e.localizedMessage}")
            }
        }
}
