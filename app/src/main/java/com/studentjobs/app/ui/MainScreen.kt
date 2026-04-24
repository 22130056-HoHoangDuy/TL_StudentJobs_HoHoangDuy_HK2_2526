package com.studentjobs.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.studentjobs.app.navigation.MainNavGraph
import com.studentjobs.app.ui.components.BottomBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
        MainNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
