package com.example.agroinnovexa20.ui.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agroinnovexa20.data.model.model3.CropType

@Composable
fun CropAdvisoryScreen(
    navController: NavHostController,
    viewModel: CropAdvisoryViewModel = viewModel()
) {
    val temp =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Double>("temp")

    val humidity =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Int>("humidity")
    val crop = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<CropType>("crop")


    LaunchedEffect(Unit) {
        if (temp != null && humidity != null && crop != null) {
            viewModel.generateAdvisory(
                crop = crop,
                temp = temp,
                humidity = humidity
            )
        }

    }

    val result = viewModel.advisory.value

    result?.let {

        Column(
            modifier = Modifier.Companion
                .padding(16.dp)
        ) {

            // Screen Title
            Text(
                text = "Crop Advisory",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.Companion.padding(12.dp))

            // Main Advisory Card
            Card(
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(
                    modifier = Modifier.Companion.padding(16.dp)
                ) {

                    // Crop Name
                    Text(
                        text = it.crop,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.Companion.padding(8.dp))
                    Text(
                        text = "Risk",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    // Risk Badge
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = it.riskLevel.uppercase()
                            )
                        }
                    )

                    Spacer(modifier = Modifier.Companion.padding(12.dp))

                    // Advisory Message
                    Text(
                        text = it.message,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.Companion.padding(12.dp))



                    Spacer(modifier = Modifier.Companion.padding(8.dp))

                    Text(
                        text = "Why this advice?",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.Companion.padding(4.dp))

                    Text(
                        text = it.reason,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

}