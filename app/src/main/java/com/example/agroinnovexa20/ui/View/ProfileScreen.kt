package com.example.agroinnovexa20.ui.View

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PestScanScreen(navController: NavHostController) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showResult by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        showResult = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pest Scan", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔥 IMAGE BOX (SCAN AREA)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {

                if (imageUri == null) {
                    Text("No Image Selected", color = Color.White)
                } else {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 BUTTONS
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Button(onClick = {
                    launcher.launch("image/*")
                }) {
                    Text("Upload Image")
                }

                Button(onClick = {
                    launcher.launch("image/*") // same for now
                }) {
                    Text("Scan Camera")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 RESULT CARD (FAKE ML OUTPUT)
            if (showResult && imageUri != null) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            "Scan Result: Leaf Blight",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Confidence: 94%")

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Status: Moderate Infection", color = Color.Red)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            "Recommended Treatment",
                            fontWeight = FontWeight.Bold
                        )

                        Text("• Use organic fungicide")
                        Text("• Avoid overwatering")
                        Text("• Remove infected leaves")

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Detailed Report")
                        }
                    }
                }
            }
        }
    }
}