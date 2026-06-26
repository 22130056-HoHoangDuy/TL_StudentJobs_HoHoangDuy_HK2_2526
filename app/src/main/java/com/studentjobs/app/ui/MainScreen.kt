package com.studentjobs.app.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.studentjobs.app.navigation.MainNavGraph
import com.studentjobs.app.session.UserSession
import com.studentjobs.app.ui.components.NotificationTopBar

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    DisposableEffect(Unit) {
        Log.d("MAIN_SCREEN", "CREATE")
        onDispose {
            Log.d("MAIN_SCREEN", "DESTROY")
        }
    }

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        if (UserSession.openProfileAfterGate) {
            UserSession.openProfileAfterGate = false
            navController.navigate("profile") {
                launchSingleTop = true
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            NotificationTopBar(
                onNotificationClick = {
                    navController.navigate("notification")
                }
            )
        }, // <-- SỬA LỖI: Thêm dấu phẩy ở đây
        bottomBar = {
            Surface(
                color = Color(0xFFF8FAFC),
                tonalElevation = 0.dp
            ) {
                NavigationBar(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {

                    // ========================================
                    // 1. JOBS (VIỆC LÀM) - DEEP BLUE
                    // ========================================
                    val isJobsSelected = currentRoute == "jobs" ||
                            currentRoute.startsWith("job_detail") ||
                            currentRoute == "create_job"
                    val jobsColor = Color(0xFF2563EB)

                    NavigationBarItem(
                        selected = isJobsSelected,
                        onClick = {
                            navController.navigate("jobs") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Work, contentDescription = null) },
                        label = {
                            Text(
                                "Việc làm",
                                fontWeight = if (isJobsSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray.copy(alpha = 0.7f),
                            selectedTextColor = jobsColor,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = jobsColor
                        )
                    )

                    // ========================================
                    // 2. TRUST (UY TÍN) - DEEP ORANGE
                    // ========================================
                    val isTrustSelected = currentRoute == "trust"
                    val trustColor = Color(0xFFEA580C)

                    NavigationBarItem(
                        selected = isTrustSelected,
                        onClick = {
                            navController.navigate("trust") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = {
                            Text(
                                "Uy tín",
                                fontWeight = if (isTrustSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray.copy(alpha = 0.7f),
                            selectedTextColor = trustColor,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = trustColor
                        )
                    )

                    // ========================================
                    // 3. HISTORY (HOẠT ĐỘNG) - VIBRANT PURPLE
                    // ========================================
                    val isHistorySelected = currentRoute == "history" ||
                            currentRoute == "my_applications"
                    val historyColor = Color(0xFF7C3AED)

                    NavigationBarItem(
                        selected = isHistorySelected,
                        onClick = {
                            navController.navigate("history") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = {
                            Text(
                                "Hoạt động",
                                fontWeight = if (isHistorySelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray.copy(alpha = 0.7f),
                            selectedTextColor = historyColor,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = historyColor
                        )
                    )

                    // ========================================
                    // 4. PROFILE (HỒ SƠ) - FRESH GREEN
                    // ========================================
                    val isProfileSelected = currentRoute == "profile" ||
                            currentRoute == "schedule" ||
                            currentRoute == "schedule_upload" ||
                            currentRoute == "manage_skills" ||
                            currentRoute == "location_picker" ||
                            currentRoute == "student_verification" ||
                            currentRoute.startsWith("phone_verification") ||
                            currentRoute.startsWith("email_verification") ||
                            currentRoute.startsWith("subscription")
                    val profileColor = Color(0xFF16A34A)

                    NavigationBarItem(
                        selected = isProfileSelected,
                        onClick = {
                            navController.navigate("profile") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = {
                            Text(
                                "Hồ sơ",
                                fontWeight = if (isProfileSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray.copy(alpha = 0.7f),
                            selectedTextColor = profileColor,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = profileColor
                        )
                    )
                }
            }
        } // <-- Đóng ngoặc Scaffold tại đây sau khi truyền xong bottomBar
    ) { padding -> // Content lambda nhận innerPadding từ Scaffold
        Box(
            modifier = Modifier
                .background(Color(0xFFF8FAFC))
                .padding(padding)
        ) {
            MainNavGraph(
                navController = navController,
                onLogout = onLogout
            )
        }
    }
}