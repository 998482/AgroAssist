package com.example.agroinnovexa20.ui.View

import android.app.Activity
import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.agroinnovexa20.ui.utils.getLocalString
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agroinnovexa20.R
import com.example.agroinnovexa20.viewModel.AiAdvisoryViewModel
import java.util.Locale

private val GP = Color(0xFF3B6D11)
private val GD = Color(0xFF27500A)
private val GM = Color(0xFF639922)
private val GL = Color(0xFFEAF3DE)
private val GB = Color(0xFFC0DD97)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropAdvisoryScreen(
    navController: NavHostController,
    viewModel: AiAdvisoryViewModel = viewModel()
) {
    val context = LocalContext.current

    // ── Data fetch — bilkul nahi badla ─────────────────────────
    val temp     = navController.currentBackStackEntry
        ?.savedStateHandle?.get<Double>("temp")
    val humidity = navController.currentBackStackEntry
        ?.savedStateHandle?.get<Int>("humidity")
    val cropName = navController.currentBackStackEntry
        ?.savedStateHandle?.get<String>("cropName")

    LaunchedEffect(Unit) {
        Log.d("DEBUG", "temp=$temp humidity=$humidity cropName=$cropName")
        if (temp != null && humidity != null && !cropName.isNullOrEmpty()) {
            viewModel.generateAdvisory(
                cropName = cropName,
                temp     = temp,
                humidity = humidity
            )
        } else {
            Log.d("DEBUG", "DATA NULL HAI")
        }
    }

    val result   by viewModel.advisory.collectAsState()
    val loading  by viewModel.loading.collectAsState()
    val error    by viewModel.error.collectAsState()
    val language by viewModel.language.collectAsState()
    // ──────────────────────────────────────────────────────────

    var tts        by remember { mutableStateOf<TextToSpeech?>(null) }
    var isSpeaking by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = if (language == "hi") Locale("hi", "IN") else Locale.US
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    fun speak(text: String) {
        tts?.language = if (language == "hi") Locale("hi", "IN") else Locale.US
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "advisory_tts")
        isSpeaking = true
    }

    fun stopSpeaking() {
        tts?.stop()
        isSpeaking = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text  = stringResource(R.string.advisory_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector    = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back),
                            tint           = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = GP,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = GP) {
                NavigationBarItem(
                    selected = false,
                    onClick  = { navController.popBackStack() },
                    icon     = {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    },
                    label  = { Text(stringResource(R.string.nav_home)) },
                    colors = navColors()
                )
                NavigationBarItem(
                    selected = true,
                    onClick  = {},
                    icon     = {
                        Icon(Icons.Default.List, contentDescription = null)
                    },
                    label  = { Text(stringResource(R.string.nav_advice)) },
                    colors = navColors()
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Loading ────────────────────────────────────────
            if (loading) {
                item {
                    Box(
                        modifier        = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = GP)
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text     = stringResource(R.string.advisory_loading),
                                fontSize = 13.sp,
                                color    = GM
                            )
                        }
                    }
                }
            }

            // ── Fallback — data null ───────────────────────────
            if (!loading && temp == null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = Color(0xFFFAEEDA)
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFFE0C080))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text       = stringResource(R.string.fallback_title),
                                fontWeight = FontWeight.Bold,
                                fontSize   = 14.sp,
                                color      = Color(0xFF633806)
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text       = stringResource(R.string.fallback_body),
                                fontSize   = 13.sp,
                                color      = Color(0xFF633806),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // ── Error ──────────────────────────────────────────
            error?.let {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = Color(0xFFFCEBEB)
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFFF09595))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text       = it,
                                fontSize   = 13.sp,
                                color      = Color(0xFF791F1F),
                                lineHeight = 20.sp
                            )
                            Spacer(Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = {
                                    if (temp != null && humidity != null &&
                                        !cropName.isNullOrEmpty()
                                    ) {
                                        viewModel.generateAdvisory(
                                            cropName = cropName,
                                            temp     = temp,
                                            humidity = humidity
                                        )
                                    }
                                },
                                shape  = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFA32D2D)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFA32D2D)
                                )
                            ) {
                                Text(
                                    text     = stringResource(R.string.error_retry),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // ── Result ─────────────────────────────────────────
            result?.let { data ->

                // Crop strip
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(10.dp),
                        colors   = CardDefaults.cardColors(containerColor = GL)
                    ) {
                        Row(
                            modifier         = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(42.dp),
                                shape    = RoundedCornerShape(50),
                                color    = GP
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(data.emoji, fontSize = 20.sp)
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text       = data.crop,
                                    fontWeight = FontWeight.Medium,
                                    fontSize   = 14.sp,
                                    color      = GD
                                )
                                Text(
                                    text     = "Temp: ${temp}°C  •  Nami: ${humidity}%",
                                    fontSize = 11.sp,
                                    color    = GM
                                )
                            }
                            RiskBadge(riskLevel = data.riskLevel, context = context)
                        }
                    }
                }

                // Stat cards
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AdvisoryStatCard(
                            emoji    = "🌡",
                            value    = "${temp}°C",
                            label    = stringResource(R.string.advisory_stat_temp),
                            modifier = Modifier.weight(1f)
                        )
                        AdvisoryStatCard(
                            emoji    = "💧",
                            value    = "${humidity}%",
                            label    = stringResource(R.string.advisory_stat_humidity),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Advisory message
                item {
                    AdvisoryInfoCard(
                        emoji   = "📋",
                        title   = stringResource(R.string.advisory_card_advice),
                        content = data.message
                    )
                }

                // Reason
                item {
                    AdvisoryInfoCard(
                        emoji   = "💡",
                        title   = stringResource(R.string.advisory_card_reason),
                        content = data.reason
                    )
                }

                // Tip — sirf tab dikhe jab ho
                if (data.tip.isNotEmpty()) {
                    item {
                        AdvisoryInfoCard(
                            emoji   = "🌿",
                            title   = stringResource(R.string.advisory_card_tip),
                            content = data.tip
                        )
                    }
                }

                // TTS button
                item {
                    val speakText = "${data.message}. ${data.reason}." +
                            if (data.tip.isNotEmpty()) " ${data.tip}" else ""

                    Button(
                        onClick  = {
                            if (isSpeaking) stopSpeaking() else speak(speakText)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = GP)
                    ) {
                        Icon(
                            imageVector    = if (isSpeaking)
                                Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint           = Color.White
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text     = if (isSpeaking)
                                stringResource(R.string.btn_stop)
                            else
                                stringResource(R.string.btn_speak),
                            color    = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                // AI badge
                item {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GL
                        ) {
                            Text(
                                text     = stringResource(R.string.label_ai_based),
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical   = 5.dp
                                ),
                                fontSize = 11.sp,
                                color    = GD
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────

@Composable
private fun AdvisoryStatCard(
    emoji    : String,
    value    : String,
    label    : String,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(10.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        border   = BorderStroke(1.5.dp, GB)
    ) {
        Column(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text       = value,
                fontWeight = FontWeight.Medium,
                fontSize   = 16.sp,
                color      = GD
            )
            Text(
                text     = label,
                fontSize = 11.sp,
                color    = GM
            )
        }
    }
}

@Composable
private fun AdvisoryInfoCard(
    emoji   : String,
    title   : String,
    content : String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        border   = BorderStroke(1.5.dp, GB)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 16.sp)
                Spacer(Modifier.width(6.dp))
                Text(
                    text       = title,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 14.sp,
                    color      = GD
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text       = content,
                fontSize   = 13.sp,
                color      = Color(0xFF444441),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun RiskBadge(riskLevel: String, context: Context) {
    val (bg, fg, label) = when (riskLevel.lowercase()) {
        "high", "zyada"     -> Triple(
            Color(0xFFFCEBEB),
            Color(0xFF791F1F),
            context.getString(R.string.risk_high)
        )
        "medium", "madhyam" -> Triple(
            Color(0xFFFAEEDA),
            Color(0xFF633806),
            context.getString(R.string.risk_medium)
        )
        else                -> Triple(
            Color(0xFFEAF3DE),
            Color(0xFF27500A),
            context.getString(R.string.risk_low)
        )
    }
    Surface(shape = RoundedCornerShape(20.dp), color = bg) {
        Text(
            text     = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color    = fg
        )
    }
}

@Composable
private fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor   = GB,
    selectedTextColor   = GB,
    unselectedIconColor = Color.White,
    unselectedTextColor = Color.White,
    indicatorColor      = GD
)