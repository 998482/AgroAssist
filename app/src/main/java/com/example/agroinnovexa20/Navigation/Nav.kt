package com.example.agroinnovexa20.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.agroinnovexa20.View.LoginPage

import com.example.agroinnovexa20.View.SignUpPage
import com.example.agroinnovexa20.View.YourScreen
import com.example.agroinnovexa20.ViewModel.MyViewModel


@Composable
fun Nav(authViewModel: MyViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login"){
            LoginPage(navController,authViewModel)
        }
        composable("signup"){
            SignUpPage(navController,authViewModel)

        }
        composable("home"){
            YourScreen(authViewModel,navController)
        }

    }

}