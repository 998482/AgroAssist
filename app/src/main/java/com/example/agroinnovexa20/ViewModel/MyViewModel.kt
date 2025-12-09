package com.example.agroinnovexa20.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroinnovexa.ui.theme.Model.Forecast
import com.example.agroinnovexa20.Model.Repository
import com.example.agroinnovexa20.Model.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel(): ViewModel() {
    val firebaseAuth = FirebaseAuth.getInstance()
    private val repo = Repository(firebaseAuth)
    private val _authState = MutableStateFlow<Result<String>>(Result.Idle)
    var authState= _authState.asStateFlow()
    fun login(email:String,password:String){
        viewModelScope.launch {
            _authState.value=Result.Loading
            _authState.value= repo.login(email,password)
        }
    }
    fun signIn(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            if (password != confirmPassword) {
                _authState.value = Result.Error("Passwords do not match")
                return@launch
            }

            _authState.value = Result.Loading
            _authState.value = repo.signIn(email, password) // Your repo function for signup
        }
    }

   // private val repos=Repo()
    private val _weather = mutableStateOf<Forecast?>(null)
    val weather: State<Forecast?> = _weather

    private val _loading=mutableStateOf(false)
    val loading: State<Boolean> =_loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun fetch_weather(city:String){
        viewModelScope.launch {
            try{
                _loading.value=true
                _error.value=null
                val response = repo.get_data(city)
                if(response.isSuccessful){
                    _weather.value = response.body()

                }
                else{
                    _error.value = "Error: ${response.code()}"
                }

            }
            catch (e:Exception){
                _error.value = "Error: ${e.message}"
            }
            finally{
                _loading.value=false

            }
        }
    }

}

