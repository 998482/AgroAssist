package com.example.agroinnovexa20.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.agroinnovexa20.R
import com.example.agroinnovexa20.data.model.weather.Forecast
import com.example.agroinnovexa20.ui.utils.getLocalString

@Composable
fun WeatherSection(
    data: Forecast,
    onAdvisoryClick: () -> Unit,
    selectedLocale: String = "en"
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = GreenPrimary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        tint = Color(0xFFC0DD97),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${data.location.name}, ${data.location.country}",
                        color = Color(0xFFC0DD97),
                        fontSize = 11.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${data.current.temp_c}°C",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium
                )
                AsyncImage(
                    model = "https:" + data.current.condition.icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = data.current.condition.text,
                    color = Color(0xFFEAF3DE),
                    fontSize = 12.sp
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(
                icon = "💧",
                value = "${data.current.humidity}%",
                label = getLocalString(context, selectedLocale, R.string.stat_humidity),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = "💨",
                value = "${data.current.wind_kph} km/h",
                label = getLocalString(context, selectedLocale, R.string.stat_wind),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(
                icon = "🌧",
                value = getLocalString(context, selectedLocale, R.string.stat_rain_tomorrow),
                label = getLocalString(context, selectedLocale, R.string.stat_rain),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = "👁",
                value = "${data.current.vis_km} km",
                label = getLocalString(context, selectedLocale, R.string.stat_visibility),
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedButton(
            onClick = onAdvisoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, GreenPrimary),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
        ) {
            Text(
                text = getLocalString(context, selectedLocale, R.string.fasal_salah_dekho),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StatCard(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFF97C459))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF27500A)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color(0xFF639922)
            )
        }
    }
}