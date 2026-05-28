package com.example.agroinnovexa20.ui.components



import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.agroinnovexa20.ui.utils.getLocalString

@Composable
fun BottomNavBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(containerColor = Color(0xFF3B6D11)) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Place, null) },
            label = { Text("Ghar") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFC0DD97),
                selectedTextColor = Color(0xFFC0DD97),
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = Color(0xFF27500A)
            )
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = onProfileClick,
            icon = { Icon(Icons.Outlined.AccountCircle, null) },
            label = { Text("Mera") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFC0DD97),
                selectedTextColor = Color(0xFFC0DD97),
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = Color(0xFF27500A)
            )
        )
    }
}