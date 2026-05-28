package com.example.agroinnovexa20.ui.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import com.example.agroinnovexa20.ui.utils.getLocalString
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.agroinnovexa20.ui.View.getString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agroinnovexa20.R
import com.example.agroinnovexa20.core.result.Result
import com.example.agroinnovexa20.ui.Navigation.Routes
import com.example.agroinnovexa20.viewModel.AuthViewModel

@Composable
fun SignUpPage(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val context = LocalContext.current

    // LANGUAGE
    var selectedLocale by remember {
        mutableStateOf("en")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val languages = listOf(
        "English" to "en",
        "हिंदी" to "hi"
    )

    // FORM STATE
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    // PASSWORD VISIBILITY
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    // AUTH STATE
    val authState =
        authViewModel
            .authState
            .collectAsState()

    val state = authState.value

    // OBSERVE AUTH STATE
    LaunchedEffect(state) {

        when (state) {

            is Result.Success -> {

                Toast.makeText(
                    context,
                    getString(
                        context,
                        selectedLocale,
                        R.string.signup_success
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                authViewModel.resetState()

                navController.navigate(
                    Routes.Home.route
                ) {

                    popUpTo(
                        Routes.Signup.route
                    ) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }

            is Result.Error -> {

                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_LONG
                ).show()

                authViewModel.resetState()
            }

            else -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // BACKGROUND IMAGE
        Image(
            painter = painterResource(
                id = R.drawable.farmside
            ),

            contentDescription = "Background",

            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            // LANGUAGE SELECTOR
            Box(
                modifier = Modifier.fillMaxWidth(),

                contentAlignment =
                    Alignment.TopEnd
            ) {

                Icon(
                    imageVector = Icons.Default.Public,

                    contentDescription = "Language",

                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.7f)
                        )
                        .padding(8.dp)
                        .clickable {
                            expanded = true
                        }
                )

                DropdownMenu(
                    expanded = expanded,

                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    languages.forEach { (label, code) ->

                        DropdownMenuItem(

                            text = {
                                Text(label)
                            },

                            onClick = {

                                selectedLocale = code
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // MAIN CARD
            Card(

                modifier =
                    Modifier.fillMaxWidth(0.9f),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White.copy(alpha = 0.85f)
                    )
            ) {

                Column(

                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    // TITLE
                    Text(
                        text = getString(
                            context,
                            selectedLocale,
                            R.string.create_account
                        ),

                        fontSize = 28.sp,

                        fontStyle =
                            FontStyle.Italic
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    // SUBTITLE
                    Text(
                        text = getString(
                            context,
                            selectedLocale,
                            R.string.signup_info
                        ),

                        fontSize = 16.sp
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // EMAIL FIELD
                    OutlinedTextField(

                        value = email,

                        onValueChange = {
                            email = it.trim()
                        },

                        label = {

                            Text(
                                getString(
                                    context,
                                    selectedLocale,
                                    R.string.email
                                )
                            )
                        },

                        singleLine = true,

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Email
                            ),

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // PASSWORD FIELD
                    OutlinedTextField(

                        value = password,

                        onValueChange = {
                            password = it
                        },

                        label = {

                            Text(
                                getString(
                                    context,
                                    selectedLocale,
                                    R.string.password_hint
                                )
                            )
                        },

                        singleLine = true,

                        visualTransformation =

                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Password
                            ),

                        trailingIcon = {

                            val icon =

                                if (passwordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility

                            IconButton(

                                onClick = {

                                    passwordVisible =
                                        !passwordVisible
                                }
                            ) {

                                Icon(
                                    imageVector = icon,

                                    contentDescription =
                                        "Password Visibility"
                                )
                            }
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // CONFIRM PASSWORD
                    OutlinedTextField(

                        value = confirmPassword,

                        onValueChange = {
                            confirmPassword = it
                        },

                        label = {

                            Text(
                                getString(
                                    context,
                                    selectedLocale,
                                    R.string.confirm_password
                                )
                            )
                        },

                        singleLine = true,

                        visualTransformation =

                            if (confirmPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Password
                            ),

                        trailingIcon = {

                            val icon =

                                if (confirmPasswordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility

                            IconButton(

                                onClick = {

                                    confirmPasswordVisible =
                                        !confirmPasswordVisible
                                }
                            ) {

                                Icon(
                                    imageVector = icon,

                                    contentDescription =
                                        "Confirm Password Visibility"
                                )
                            }
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(32.dp)
                    )

                    // SIGNUP BUTTON
                    Button(

                        onClick = {

                            authViewModel.signUp(
                                email,
                                password,
                                confirmPassword
                            )
                        },

                        enabled =
                            state !is Result.Loading,

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        if(state is Result.Loading){

                            CircularProgressIndicator()

                        } else {

                            Text(
                                text = getString(
                                    context,
                                    selectedLocale,
                                    R.string.signup
                                )
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // LOGIN NAVIGATION
                    TextButton(

                        onClick = {

                            navController.navigate(
                                Routes.Login.route
                            ) {

                                launchSingleTop = true
                            }
                        }
                    ) {

                        Text(
                            text = getString(
                                context,
                                selectedLocale,
                                R.string.already_account
                            )
                        )
                    }
                }
            }
        }
    }
}