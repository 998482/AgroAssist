package com.example.agroinnovexa20.viewModel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import android.location.Geocoder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _location = MutableStateFlow("")
    val location = _location.asStateFlow()

    private val _crop = MutableStateFlow("")
    val crop = _crop.asStateFlow()

    private val _selectedLocale = MutableStateFlow("en")
    val selectedLocale = _selectedLocale.asStateFlow()

    // ── GPS coordinates — WeatherAPI directly use karega ──────
    private val _gpsLatLng = MutableStateFlow<String?>(null)
    val gpsLatLng = _gpsLatLng.asStateFlow()

    fun onLocationChange(value: String) {
        _location.value = value
        _gpsLatLng.value = null  // User ne manually type kiya — GPS reset
    }

    fun onCropChange(value: String) { _crop.value = value }
    fun onLocaleChange(code: String) { _selectedLocale.value = code }

    fun onSpeechResult(result: String, isLocation: Boolean) {
        if (isLocation) {
            _location.value = result
            _gpsLatLng.value = null  // Voice input — GPS reset
        } else {
            _crop.value = result
        }
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private val _gpsLoading = MutableStateFlow(false)
    val gpsLoading = _gpsLoading.asStateFlow()

    fun fetchCurrentLocation(context: Context) {
        _gpsLoading.value = true          // ← loading start
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { loc ->
                _gpsLoading.value = false  // ← loading stop
                if (loc != null) {
                    val latLng = "${loc.latitude},${loc.longitude}"
                    _gpsLatLng.value = latLng
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        @Suppress("DEPRECATION")
                        val city = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                            ?.firstOrNull()?.locality ?: latLng
                        _location.value = city
                    } catch (e: Exception) {
                        _location.value = latLng
                    }
                }
            }.addOnFailureListener {
                _gpsLoading.value = false  // ← loading stop on fail
            }
        } catch (e: SecurityException) {
            _gpsLoading.value = false
        }
    }




    // REPLACE with yeh:
    suspend fun getQueryForWeather(context: Context): String {
        val gps = _gpsLatLng.value
        if (gps != null) return gps  // GPS available — directly use

        val typedText = _location.value.trim()
        if (typedText.isEmpty()) return typedText

        // Hindi ya koi bhi text — Geocoder se lat/lng lo
        return try {
            withContext(kotlinx.coroutines.Dispatchers.IO) {
                val geocoder = Geocoder(context, java.util.Locale.ENGLISH)
                @Suppress("DEPRECATION")
                val results = geocoder.getFromLocationName(typedText, 1)
                if (!results.isNullOrEmpty()) {
                    val lat = results[0].latitude
                    val lng = results[0].longitude
                    "$lat,$lng"  // WeatherAPI seedha lat,lng accept karta hai
                } else {
                    typedText  // fallback — original text
                }
            }
        } catch (e: Exception) {
            typedText  // fallback
        }
    }
}