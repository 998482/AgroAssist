package com.example.agroinnovexa20.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agroinnovexa20.ui.View.CropAdvisoryScreen

import com.example.agroinnovexa20.ui.View.LoginPage
import com.example.agroinnovexa20.ui.View.PestScanScreen
import com.example.agroinnovexa20.ui.View.ProfileScreen

import com.example.agroinnovexa20.ui.View.SignUpPage
import com.example.agroinnovexa20.ui.View.YourScreen
import com.example.agroinnovexa20.viewModel.MyViewModel


@Composable
fun Nav(authViewModel: MyViewModel){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginPage(navController, authViewModel)
        }

        composable(Routes.Signup.route) {
            SignUpPage(navController, authViewModel)
        }

        composable(Routes.Home.route) {
            YourScreen(authViewModel, navController)
        }

        composable(Routes.CropAdvisory.route) {
            CropAdvisoryScreen(navController)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController)
        }

        composable(Routes.PestScan.route) {
            PestScanScreen(navController)
        }

    }

}

