package com.example.agroinnovexa20.ai.ui



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agroinnovexa20.ai.viewmodel.AiViewModel

@Composable
fun AiTestScreen() {

    val vm: AiViewModel = viewModel()

    val response by vm.response.collectAsState()

    var prompt by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(
            value = prompt,
            onValueChange = {
                prompt = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vm.askAi(prompt)
            }
        ) {
            Text("Ask AI")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(response)
    }
}