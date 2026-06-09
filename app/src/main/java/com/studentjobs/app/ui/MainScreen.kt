package com.studentjobs.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.studentjobs.app.navigation.MainNavGraph

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {

    val navController = rememberNavController()

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry
            ?.destination
            ?.route

    Scaffold(

        bottomBar = {

            NavigationBar(

                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    ),

                containerColor = Color.White,

                tonalElevation = 10.dp

            ) {

                // ========================================
                // JOBS
                // ========================================

                NavigationBarItem(

                    selected =
                        currentRoute == "jobs",

                    onClick = {

                        navController.navigate(
                            "jobs"
                        ) {

                            launchSingleTop = true

                            restoreState = true

                            popUpTo(
                                navController
                                    .graph
                                    .startDestinationId
                            ) {

                                saveState = true
                            }
                        }
                    },

                    icon = {

                        Icon(

                            imageVector =
                                Icons.Default.Work,

                            contentDescription = null
                        )
                    },

                    label = {

                        Text("Việc làm")
                    },

                    colors =
                        NavigationBarItemDefaults
                            .colors(

                                indicatorColor =
                                    Color(
                                        0xFFE0E7FF
                                    )
                            )
                )

                // ========================================
                // TRUST
                // ========================================

                NavigationBarItem(

                    selected =
                        currentRoute == "trust",

                    onClick = {

                        navController.navigate(
                            "trust"
                        ) {

                            launchSingleTop = true

                            restoreState = true

                            popUpTo(
                                navController
                                    .graph
                                    .startDestinationId
                            ) {

                                saveState = true
                            }
                        }
                    },

                    icon = {

                        Icon(

                            imageVector =
                                Icons.Default.Star,

                            contentDescription = null
                        )
                    },

                    label = {

                        Text("Uy tín")
                    },

                    colors =
                        NavigationBarItemDefaults
                            .colors(

                                indicatorColor =
                                    Color(
                                        0xFFE0E7FF
                                    )
                            )
                )

                // ========================================
                // HISTORY
                // ========================================

                NavigationBarItem(

                    selected =
                        currentRoute == "history",

                    onClick = {

                        navController.navigate(
                            "history"
                        ) {

                            launchSingleTop = true

                            restoreState = true

                            popUpTo(
                                navController
                                    .graph
                                    .startDestinationId
                            ) {

                                saveState = true
                            }
                        }
                    },

                    icon = {

                        Icon(

                            imageVector =
                                Icons.Default.History,

                            contentDescription = null
                        )
                    },

                    label = {

                        Text("Hoạt động")
                    },

                    colors =
                        NavigationBarItemDefaults
                            .colors(

                                indicatorColor =
                                    Color(
                                        0xFFE0E7FF
                                    )
                            )
                )

                // ========================================
                // PROFILE
                // ========================================

                NavigationBarItem(

                    selected =
                        currentRoute == "profile",

                    onClick = {

                        navController.navigate(
                            "profile"
                        ) {

                            launchSingleTop = true

                            restoreState = true

                            popUpTo(
                                navController
                                    .graph
                                    .startDestinationId
                            ) {

                                saveState = true
                            }
                        }
                    },

                    icon = {

                        Icon(

                            imageVector =
                                Icons.Default.Person,

                            contentDescription = null
                        )
                    },

                    label = {

                        Text("Hồ sơ")
                    },

                    colors =
                        NavigationBarItemDefaults
                            .colors(

                                indicatorColor =
                                    Color(
                                        0xFFE0E7FF
                                    )
                            )
                )
            }
        }

    ) { padding ->

        MainNavGraph(

            navController = navController,

            modifier =
                Modifier.padding(
                    padding
                ),
            onLogout = onLogout
        )
    }
}