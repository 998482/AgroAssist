package com.example.agroinnovexa20.ui.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.agroinnovexa20.R
import com.example.agroinnovexa20.viewModel.MyViewModel
import com.example.agroinnovexa.ui.theme.Model.Forecast
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.example.agroinnovexa20.data.model.model3.CropType
import com.example.agroinnovexa20.ui.Navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourScreen(view: MyViewModel, navHostController: NavHostController) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current



    // Language selection state
    var selectedLocale by remember { mutableStateOf("en") }
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English" to "en", "हिंदी" to "hi")

    val weatherData by view.weather
    val loading by view.loading
    val error by view.error
    var location by remember { mutableStateOf("") }
    var crop by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = getString(context, selectedLocale, R.string.hillside_weather_forecast),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {

                    // 🔥 PROFILE NAVIGATION
                    IconButton(onClick = {
                        navHostController.navigate(Routes.Profile.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.Black
                        )
                    }

                    // 🌐 LANGUAGE ICON (same as yours)
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Language",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.7f))
                            .padding(8.dp)
                            .clickable { expanded = true }
                    )

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        languages.forEach { (label, code) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedLocale = code
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        },


        bottomBar = {
            NavigationBar(containerColor = Color.White) {

                // LEFT - HOME
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Icon(Icons.Default.Place, contentDescription = "Home")
                    },
                    label = { Text("Home") }
                )


                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = {
                            navHostController.navigate(Routes.PestScan.route)
                        },
                        containerColor = Color(0xFF2E7D32)
                    ) {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = "Scan",
                            tint = Color.White
                        )
                    }
                }

                // RIGHT - PROFILE
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navHostController.navigate(Routes.Profile.route)
                    },
                    icon = {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile")
                    },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dashboard heading
            item {
                Text(
                    text = getString(context, selectedLocale, R.string.farm_dashboard),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp
                )
                Text(text = getString(context, selectedLocale, R.string.farm_hyperlocal_weather))
            }

            // Location & Crop input
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = getString(context, selectedLocale, R.string.microclimate_prediction),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 20.sp
                        )
                        Text(text = getString(context, selectedLocale, R.string.ai_forecast))
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text(getString(context, selectedLocale, R.string.location)) },
                            placeholder = { Text(getString(context, selectedLocale, R.string.location_placeholder)) }
                        )
                        OutlinedTextField(
                            value = crop,
                            onValueChange = { crop = it },
                            label = { Text(getString(context, selectedLocale, R.string.crop_type)) },
                            placeholder = { Text(getString(context, selectedLocale, R.string.crop_placeholder)) }
                        )
                        ElevatedButton(
                            onClick = {
                                focusManager.clearFocus()
                                view.fetch_weather(location)

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF388E3C)
                            )
                        ) {
                            Text(
                                text = getString(context, selectedLocale, R.string.get_forecast),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Weather Data
            item {
                if (loading) {
                    CircularProgressIndicator(color = Color.Green, strokeWidth = 4.dp)
                }
                if (error != null) {
                    Text(text = error.toString(), color = Color.Red)
                }
                if (weatherData != null) {
                    WeatherData(data = weatherData!!, locale = selectedLocale)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val selectedCrop = mapCropStringToType(crop)

                            navHostController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("temp", weatherData!!.current.temp_c)

                            navHostController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("humidity", weatherData!!.current.humidity)

                            navHostController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("crop", selectedCrop) // Pass the enum to next screen

                            navHostController.navigate(Routes.CropAdvisory.route)
                        }
                    )
                    {
                        Text("View Crop Advisory", color = Color.White)
                    }
                }

            }
        }
    }
}

@Composable
fun WeatherData(data: Forecast, locale: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {

            // Location & Live Data
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Text(
                    text = getString(context, locale, R.string.live_data),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Icon(imageVector = Icons.Default.Place, contentDescription = null, modifier = Modifier.size(15.dp))
                Text(text = data.location.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "," + data.location.country, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Text(text = getString(context, locale, R.string.up_to_minute_reading))
            Spacer(modifier = Modifier.height(16.dp))

            // Temperature & Humidity
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                WeatherCard(iconRes = R.drawable.temp, label = getString(context, locale, R.string.temperature), value = "${data.current.temp_c}°C")
                WeatherCard(iconRes = R.drawable.hum, label = getString(context, locale, R.string.humidity), value = "${data.current.humidity}%")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Wind & Condition
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                WeatherCard(iconRes = R.drawable.leaf, label = getString(context, locale, R.string.wind), value = "${data.current.wind_kph} km/h")
                Card(modifier = Modifier.width(150.dp).height(100.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        AsyncImage(model = "https:" + data.current.condition.icon, contentDescription = null, modifier = Modifier.size(50.dp))
                        Text(text = data.current.condition.text, fontWeight = FontWeight.Bold) // Always in English
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCard(iconRes: Int, label: String, value: String) {
    Card(modifier = Modifier.size(140.dp, 80.dp)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(iconRes), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label)
                Text(text = value, fontWeight = FontWeight.Bold)
            }
        }
    }
}
fun mapCropStringToType(crop: String): CropType {
    return when(crop.trim().lowercase()) {
        "tomato", "टमाटर" -> CropType.TOMATO
        "wheat", "गेहूं" -> CropType.WHEAT
        "rice", "धान" -> CropType.RICE
        else -> CropType.TOMATO // fallback default
    }
}
