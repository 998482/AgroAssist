package com.example.agroinnovexa20.viewModel


import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _location = MutableStateFlow("")
    val location = _location.asStateFlow()

    private val _crop = MutableStateFlow("")
    val crop = _crop.asStateFlow()

    private val _selectedLocale = MutableStateFlow("en")
    val selectedLocale = _selectedLocale.asStateFlow()

    fun onLocationChange(value: String) { _location.value = value }
    fun onCropChange(value: String) { _crop.value = value }
    fun onLocaleChange(code: String) { _selectedLocale.value = code }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun fetchCurrentLocation(context: Context) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { loc ->
                if (loc != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val city = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                        ?.firstOrNull()?.locality
                        ?: "${loc.latitude},${loc.longitude}"
                    _location.value = city
                }
            }
        } catch (e: SecurityException) { /* permission nahi hai */ }
    }

    fun onSpeechResult(result: String, isLocation: Boolean) {
        if (isLocation) _location.value = result
        else _crop.value = result
    }
}