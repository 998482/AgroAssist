package com.example.agroinnovexa20.ui.View

import ProfileViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import com.example.agroinnovexa20.ui.utils.getLocalString
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agroinnovexa20.R
import com.example.agroinnovexa20.ui.Navigation.Routes
import com.example.agroinnovexa20.ui.components.BottomNavBar
import com.example.agroinnovexa20.viewModel.AuthViewModel

private val GP = Color(0xFF3B6D11)
private val GD = Color(0xFF27500A)
private val GM = Color(0xFF639922)
private val GL = Color(0xFFEAF3DE)
private val GB = Color(0xFFC0DD97)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel = viewModel(),
    selectedLocale: String = "en"
) {
    val context = LocalContext.current
    val uiState by profileViewModel.uiState.collectAsState()
    val currentEmail = authViewModel.getCurrentUserEmail()

    var showEditDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackbarHostState.showSnackbar(
                getString(context, selectedLocale, R.string.edit_saved_msg)
            )
            profileViewModel.resetSaved()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        getString(context, selectedLocale, R.string.profile_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GP,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "profile",
                onHomeClick = { navController.navigate(Routes.Home.route) },
                onProfileClick = {}
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Header ──────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(Brush.verticalGradient(listOf(GP, GM)))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .offset(x = (-30).dp, y = (-20).dp)
                                .background(Color.White.copy(alpha = 0.07f), CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 20.dp, y = 10.dp)
                                .background(Color.White.copy(alpha = 0.07f), CircleShape)
                        )
                        Text(
                            "🌾", fontSize = 40.sp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = 20.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(3.dp, GP, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.name.trim().firstOrNull()
                                ?.uppercaseChar()?.toString()
                                ?: currentEmail?.trim()?.firstOrNull()
                                    ?.uppercaseChar()?.toString()
                                ?: "👤",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = GP
                        )
                    }
                }
            }

            // ── Name + email ────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 8.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = uiState.name.ifEmpty {
                            getString(context, selectedLocale, R.string.profile_default_name)
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GD
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = currentEmail
                            ?: getString(context, selectedLocale, R.string.profile_not_logged_in),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(20.dp), color = GL) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = GP,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                getString(context, selectedLocale, R.string.profile_verified),
                                fontSize = 11.sp,
                                color = GD
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // ── Account section ─────────────────────────────────
            item {
                SectionHeader(
                    getString(context, selectedLocale, R.string.profile_section_account)
                )
            }

            item {
                ProfileMenuCard {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Person,
                        title = getString(context, selectedLocale, R.string.profile_personal_info),
                        subtitle = uiState.name.ifEmpty {
                            getString(context, selectedLocale, R.string.profile_personal_info_sub)
                        },
                        onClick = { showEditDialog = true }
                    )
                    MenuDivider()
                    ProfileMenuItem(
                        icon = Icons.Outlined.Lock,
                        title = getString(context, selectedLocale, R.string.profile_change_password),
                        subtitle = getString(context, selectedLocale, R.string.profile_change_password_sub),
                        onClick = { showPasswordDialog = true }
                    )
                    MenuDivider()
                    ProfileMenuItem(
                        icon = Icons.Outlined.LocationOn,
                        title = getString(context, selectedLocale, R.string.profile_my_farm),
                        subtitle = uiState.farmLocation.ifEmpty {
                            getString(context, selectedLocale, R.string.profile_my_farm_sub)
                        },
                        onClick = { showEditDialog = true }
                    )
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            // ── Logout ──────────────────────────────────────────
            item {
                Button(
                    onClick = {
                        authViewModel.logout()
                        navController.navigate(Routes.Login.route) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCEBEB)
                    )
                ) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color(0xFF791F1F)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        getString(context, selectedLocale, R.string.profile_logout),
                        color = Color(0xFF791F1F),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    // ── Edit Profile Dialog ──────────────────────────────────────
    if (showEditDialog) {
        val genders = listOf(
            getString(context, selectedLocale, R.string.gender_male),
            getString(context, selectedLocale, R.string.gender_female),
            getString(context, selectedLocale, R.string.gender_other)
        )
        var expanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    getString(context, selectedLocale, R.string.edit_profile_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { profileViewModel.onNameChange(it) },
                        label = {
                            Text(getString(context, selectedLocale, R.string.edit_name_hint))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.phone,
                        onValueChange = { profileViewModel.onPhoneChange(it) },
                        label = {
                            Text(getString(context, selectedLocale, R.string.edit_phone_hint))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.gender,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(getString(context, selectedLocale, R.string.edit_gender_hint))
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            genders.forEach { g ->
                                DropdownMenuItem(
                                    text = { Text(g) },
                                    onClick = {
                                        profileViewModel.onGenderChange(g)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = uiState.farmLocation,
                        onValueChange = { profileViewModel.onFarmLocationChange(it) },
                        label = {
                            Text(getString(context, selectedLocale, R.string.edit_farm_hint))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        profileViewModel.saveProfile()
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GP)
                ) {
                    Text(
                        getString(context, selectedLocale, R.string.edit_save),
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text(
                        getString(context, selectedLocale, R.string.edit_cancel),
                        color = GP
                    )
                }
            }
        )
    }

    // ── Change Password Dialog ───────────────────────────────────
    if (showPasswordDialog) {
        var newPass by remember { mutableStateOf("") }
        var confirmPass by remember { mutableStateOf("") }
        var passError by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = {
                Text(
                    getString(context, selectedLocale, R.string.profile_change_password),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = {
                            Text(getString(context, selectedLocale, R.string.password_new))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmPass,
                        onValueChange = { confirmPass = it },
                        label = {
                            Text(getString(context, selectedLocale, R.string.password_confirm))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passError.isNotEmpty()) {
                        Text(passError, color = Color(0xFFA32D2D), fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when {
                            newPass.length < 6 -> passError =
                                getString(context, selectedLocale, R.string.password_min_length)
                            newPass != confirmPass -> passError =
                                getString(context, selectedLocale, R.string.password_no_match)
                            else -> {
                                authViewModel.changePassword(
                                    newPass,
                                    onSuccess = { showPasswordDialog = false },
                                    onError = { passError = it }
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GP)
                ) {
                    Text(
                        getString(context, selectedLocale, R.string.edit_save),
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text(
                        getString(context, selectedLocale, R.string.edit_cancel),
                        color = GP
                    )
                }
            }
        )
    }
}

// ── Sub-composables — same as before ─────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = GM,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
    )
}

@Composable
private fun ProfileMenuCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, GB)
    ) {
        Column(content = content)
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(onClick = onClick, color = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(10.dp),
                color = GL
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = GP,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = GD)
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = GL,
        thickness = 1.dp
    )
}