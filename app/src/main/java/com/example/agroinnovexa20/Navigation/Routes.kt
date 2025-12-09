package com.example.agroinnovexa20.Navigation

sealed class Routes {
    object Login : Routes()
    object Signup : Routes()
    object Home : Routes()
    object Forecast : Routes()

}