package com.example.agroinnovexa20.ui.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale

@Composable
fun SpeakButton(
    text: String,
    language: String = "hi"
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isSpeaking by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = if (language == "hi") Locale("hi", "IN") else Locale.US
                tts?.language = locale
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Button(
        onClick = {
            if (isSpeaking) {
                tts?.stop()
                isSpeaking = false
            } else {
                val locale = if (language == "hi") Locale("hi", "IN") else Locale.US
                tts?.language = locale
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                isSpeaking = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
    ) {
        Icon(
            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
            contentDescription = null,
            tint = Color.White
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (isSpeaking) "⏹ Band karein" else "🔊 Salah suniye",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}