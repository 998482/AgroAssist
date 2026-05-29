package com.example.agroinnovexa20.ui.View

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agroinnovexa20.R
import com.example.agroinnovexa20.ui.Navigation.Routes
import com.example.agroinnovexa20.ui.components.BottomNavBar
import com.example.agroinnovexa20.ui.components.InputSection
import com.example.agroinnovexa20.ui.components.WeatherSection
import com.example.agroinnovexa20.ui.utils.getLocalString
import com.example.agroinnovexa20.viewModel.HomeViewModel
import com.example.agroinnovexa20.weather.presentation.WeatherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    weatherViewModel: WeatherViewModel,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val location by homeViewModel.location.collectAsState()
    val crop by homeViewModel.crop.collectAsState()
    val selectedLocale by homeViewModel.selectedLocale.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val weatherData by weatherViewModel.weather.collectAsState()
    val loading by weatherViewModel.loading.collectAsState()
    val error by weatherViewModel.error.collectAsState()
    val scope = rememberCoroutineScope()

    val languages = listOf("English" to "en", "हिंदी" to "hi")

    // ── Speech Launchers ──────────────────────────────────────────────────────
    fun makeSpeechIntent() = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (selectedLocale == "hi") "hi-IN" else "en-IN")
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Boliye...")
    }

    val locationMicLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull() ?: ""
            homeViewModel.onSpeechResult(text, isLocation = true)
        }
    }

    val cropMicLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull() ?: ""
            homeViewModel.onSpeechResult(text, isLocation = false)
        }
    }

    // ── Permission + Auto Location ────────────────────────────────────────────
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) homeViewModel.fetchCurrentLocation(context)
    }

    LaunchedEffect(Unit) {
        if (homeViewModel.hasLocationPermission(context)) {
            homeViewModel.fetchCurrentLocation(context)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = getLocalString(context, selectedLocale, R.string.home_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B6D11),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navHostController.navigate(Routes.Profile.route) }) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = null)
                    }
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { expanded = true }
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        languages.forEach { (label, code) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    homeViewModel.onLocaleChange(code)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "home",
                onHomeClick = {},
                onProfileClick = { navHostController.navigate(Routes.Profile.route) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = getLocalString(context, selectedLocale, R.string.farm_dashboard),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27500A)
                )
            }

            item {
                val gpsLoading by homeViewModel.gpsLoading.collectAsState()

// InputSection mein:
                InputSection(
                    location = location,
                    crop = crop,
                    onLocationChange = { homeViewModel.onLocationChange(it) },
                    onCropChange = { homeViewModel.onCropChange(it) },
                    onMicLocationClick = {
                        if (SpeechRecognizer.isRecognitionAvailable(context))
                            locationMicLauncher.launch(makeSpeechIntent())
                    },
                    onMicCropClick = {
                        if (SpeechRecognizer.isRecognitionAvailable(context))
                            cropMicLauncher.launch(makeSpeechIntent())
                    },
                    onGenerateClick = {
                        focusManager.clearFocus()
                        scope.launch {
                            val query = homeViewModel.getQueryForWeather(context)
                            weatherViewModel.fetchWeather(query)
                        }
                    },
                    onGpsClick = {
                        if (homeViewModel.hasLocationPermission(context)) {
                            homeViewModel.fetchCurrentLocation(context)
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    isGpsLoading = gpsLoading,
                    selectedLocale = selectedLocale
                )
            }

            item {
                if (loading) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF3B6D11))
                    }
                }
                error?.let { Text(it, color = Color.Red, fontSize = 13.sp) }
            }

            weatherData?.let { data ->
                item {
                    WeatherSection(
                        data = data,
                        selectedLocale = selectedLocale,
                        onAdvisoryClick = {
                            navHostController.navigate(Routes.CropAdvisory.route)
                            navHostController.currentBackStackEntry
                                ?.savedStateHandle?.apply {
                                    set("temp", data.current.temp_c)
                                    set("humidity", data.current.humidity)
                                    set("cropName", crop)
                                }
                        }
                    )
                }
            }
        }
    }
}