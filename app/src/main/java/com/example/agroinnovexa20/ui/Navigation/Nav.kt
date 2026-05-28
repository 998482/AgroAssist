package com.example.agroinnovexa20.ui.Navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agroinnovexa20.ui.View.*
import com.example.agroinnovexa20.viewModel.AuthViewModel
import com.example.agroinnovexa20.viewModel.HomeViewModel
import com.example.agroinnovexa20.weather.presentation.WeatherViewModel

@Composable
fun Nav() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = viewModel() // ← ek baar yahan banao

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }

        composable(Routes.Login.route) {
            LoginPage(navController, authViewModel)
        }

        composable(Routes.Signup.route) {
            SignUpPage(navController, authViewModel)
        }

        composable(Routes.Home.route) {
            HomeScreen(weatherViewModel, navController, homeViewModel)
        }

        composable(Routes.Profile.route) {
            // selectedLocale HomeViewModel se lo
            val selectedLocale by homeViewModel.selectedLocale.collectAsState()
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                selectedLocale = selectedLocale  // ← pass karo
            )
        }

        composable(Routes.CropAdvisory.route) {
            CropAdvisoryScreen(navController)
        }


    }
}