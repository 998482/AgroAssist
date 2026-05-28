package com.example.agroinnovexa20.ui.View

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.agroinnovexa20.ui.utils.getLocalString
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.agroinnovexa20.R

private val GP  = Color(0xFF3B6D11)
private val GD  = Color(0xFF27500A)
private val GL  = Color(0xFFC0DD97)
private val GM  = Color(0xFF639922)

@Composable
fun SplashScreen(navController: NavController) {

    // ── aapka original Firebase logic — nahi badla ────────────
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000L)          // 2 sec splash

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    // ─────────────────────────────────────────────────────────

    // Logo pulse animation
    val pulse = rememberInfiniteTransition(label = "pulse")
    val scale by pulse.animateFloat(
        initialValue = 0.95f,
        targetValue  = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Loader bar animation
    val loaderAnim = rememberInfiniteTransition(label = "loader")
    val loaderWidth by loaderAnim.animateFloat(
        initialValue = 0f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loader"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GP),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(Modifier.height(1.dp))

            // ── Logo + App name ───────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f),
            ) {
                Spacer(Modifier.weight(1f))

                // Logo circle
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(GD),
                    contentAlignment = Alignment.Center
                ) {
                    // App ka icon — R.drawable.ic_launcher_foreground
                    // ya apna koi bhi plant/farm icon lagao
                    Text("🌾", fontSize = 44.sp)
                }

                Text(
                    text = stringResource(R.string.splash_app_name),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = stringResource(R.string.splash_tagline),
                    fontSize = 14.sp,
                    color = GL,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.weight(1f))
            }

            // ── Loader bar ────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 48.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(GD)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(loaderWidth)
                            .height(4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(GL)
                    )
                }


                Text(
                    text = stringResource(R.string.splash_loading),
                    fontSize = 12.sp,
                    color = GM
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.splash_version),
                    fontSize = 11.sp,
                    color = Color(0xFF639922)
                )
            }
        }
    }
}