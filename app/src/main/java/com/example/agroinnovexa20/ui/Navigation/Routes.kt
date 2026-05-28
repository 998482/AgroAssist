package com.example.agroinnovexa20.ui.Navigation



sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Signup : Routes("signup")
    object Home : Routes("home")
    object Forecast : Routes("forecast")
    object CropAdvisory : Routes("crop_advisory")
    object Profile : Routes("profile")
    object PestScan : Routes("pest_scan")
}
