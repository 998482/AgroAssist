package com.example.agroinnovexa20.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroinnovexa20.R
import com.example.agroinnovexa20.ui.utils.getLocalString

val GreenPrimary = Color(0xFF3B6D11)
val GreenLight   = Color(0xFFEAF3DE)
val GreenBorder  = Color(0xFF97C459)

@Composable
fun InputSection(
    location: String,
    crop: String,
    onLocationChange: (String) -> Unit,
    onCropChange: (String) -> Unit,
    onMicLocationClick: () -> Unit,
    onMicCropClick: () -> Unit,
    onGenerateClick: () -> Unit,
    selectedLocale: String = "en"
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        Text(
            text = getLocalString(context, selectedLocale, R.string.input_section_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = GreenPrimary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = location,
                onValueChange = onLocationChange,
                label = {
                    Text(getLocalString(context, selectedLocale, R.string.enter_location))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = GreenBorder
                ),
                singleLine = true
            )
            IconButton(
                onClick = onMicLocationClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = crop,
                onValueChange = onCropChange,
                label = {
                    Text(getLocalString(context, selectedLocale, R.string.enter_crop))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = GreenBorder
                ),
                singleLine = true
            )
            IconButton(
                onClick = onMicCropClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Button(
            onClick = onGenerateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
        ) {
            Text(
                text = getLocalString(context, selectedLocale, R.string.generate),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}