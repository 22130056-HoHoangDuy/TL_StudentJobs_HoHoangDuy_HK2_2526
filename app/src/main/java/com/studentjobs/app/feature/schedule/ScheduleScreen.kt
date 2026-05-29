package com.studentjobs.app.feature.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.feature.schedule.components.ScheduleCard

@Composable
fun ScheduleScreen(

    navController: NavController,

    currentPlan: SubscriptionPlan,

    viewModel: ScheduleViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

) {

    val uiState by
    viewModel.uiState.collectAsState()

    val uid =
        FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid

    // ========================================
    // LOAD SCHEDULE
    // ========================================

    uid?.let {

        viewModel.loadSchedule(it)
    }

    // ========================================
    // FREE USER
    // ========================================

    if (
        currentPlan !=
        SubscriptionPlan.PLUS
    ) {

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(

                text =
                    "Upload timetable is available for PLUS users only.",

                style =
                    MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Button(

                onClick = {

                    navController.navigate(
                        "subscription"
                    )
                },

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Color(0xFFFFB300)
                    )
            ) {

                Text(
                    text = "Upgrade to PLUS"
                )
            }
        }

        return
    }

    // ========================================
    // LOADING
    // ========================================

    if (uiState.isLoading) {

        Column(

            modifier = Modifier
                .fillMaxSize(),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            CircularProgressIndicator()
        }

        return
    }

    // ========================================
    // EMPTY STATE
    // ========================================

    if (
        uiState.schedule == null
    ) {

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color(0xFFEEF4FF)
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(20.dp)
                ) {

                    Text(

                        text =
                            "No timetable uploaded yet.",

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(

                        text =
                            "Upload your class timetable to activate Smart Auto Apply."
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Button(

                onClick = {

                    navController.navigate(
                        "schedule_upload"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Color(0xFF2962FF)
                    )
            ) {

                Text(
                    text = "Upload Timetable"
                )
            }
        }

        return
    }

    // ========================================
    // SHOW SCHEDULE
    // ========================================

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {

            ScheduleCard(

                schedule =
                    uiState.schedule!!
            )
        }
    }
}