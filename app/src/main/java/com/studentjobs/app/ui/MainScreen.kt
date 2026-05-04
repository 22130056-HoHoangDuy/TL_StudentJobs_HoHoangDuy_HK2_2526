package com.studentjobs.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.studentjobs.app.navigation.MainNavGraph

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("home")
                    },
                    icon = { Text("🏠") },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("jobs")
                    },
                    icon = { Text("💼") },
                    label = { Text("Jobs") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("messages")
                    },
                    icon = { Text("💬") },
                    label = { Text("Messages") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("profile")
                    },
                    icon = { Text("👤") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->

        MainNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}