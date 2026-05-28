package com.example.agroinnovexa20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.agroinnovexa20.ai.ui.AiTestScreen
import com.example.agroinnovexa20.ui.Navigation.Nav

import com.example.agroinnovexa20.ui.theme.AgroInnovexa20Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgroInnovexa20Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  Nav()




                }
            }
        }
    }
}

