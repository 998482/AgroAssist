package com.example.agroinnovexa20.Model

import com.example.agroinnovexa.ui.theme.Model.Forecast
import com.example.agroinnovexa.ui.theme.Model.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import retrofit2.Response

sealed class Result<out T>{
    data object Idle:Result<Nothing>()
    data object Loading:Result<Nothing>()
    data class Success<T>(val message:T):Result<T>()
    data class Error(val message : String):Result<Nothing>()

}
class Repository(val firebaseAuth: FirebaseAuth){
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
    suspend fun get_data(city:String):Response<Forecast>{
        return RetrofitClient.retrofit.getForecast("d21e8af2992b400186322406252409",city,6)
    }
}
