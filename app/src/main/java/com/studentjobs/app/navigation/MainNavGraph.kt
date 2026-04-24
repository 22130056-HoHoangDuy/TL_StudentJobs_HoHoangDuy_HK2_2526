package com.studentjobs.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.studentjobs.app.feature.home.HomeEntryScreen
import com.studentjobs.app.feature.profile.ProfileScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {

        composable("home") {
            HomeEntryScreen()
        }

        composable("profile") {
            ProfileScreen() // 🔥 FIX
        }

        composable("jobs") {
            Text("Jobs")
        }

        composable("messages") {
            Text("Messages")
        }
    }
}