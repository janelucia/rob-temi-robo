// Datei: app/src/main/java/com/example/rob_temi_robo_ui/MainActivity.kt
package com.example.rob_temi_robo_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rob_temi_robo_ui.ui.theme.Rob_Temi_Robo_UITheme
import com.example.rob_temi_robo_ui.ui.theme.components.CustomTopAppBar
import com.example.rob_temi_robo_ui.ui.theme.pages.GuideSelector
import com.example.rob_temi_robo_ui.ui.theme.pages.Home
import com.example.rob_temi_robo_ui.ui.theme.pages.Guide

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rob_Temi_Robo_UITheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CustomTopAppBar(navController) }
                ) { innerPadding ->
                    NavHost(navController, startDestination = "homePage") {
                        composable("homePage") { Home(innerPadding, navController) }
                        composable("guideSelector") {GuideSelector(innerPadding, navController)}
                        composable("guide") {Guide(innerPadding, navController)}
                    }
                }
            }
        }
    }
}
