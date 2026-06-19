package com.studentjobs.app.feature.home.employer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EmployerHomeScreen(

    navController: NavController

) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)

    ) {

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )

        ) {

            Column(

                modifier =
                    Modifier.padding(16.dp)

            ) {

                Text(

                    text =
                        "🏢 Employer Dashboard",

                    style =
                        MaterialTheme.typography
                            .headlineSmall
                )

                Text(

                    text =
                        "Manage jobs and recruit students."
                )
            }
        }

        Button(

            modifier =
                Modifier.fillMaxWidth(),

            onClick = {

                navController.navigate(
                    "create_job"
                )
            }

        ) {

            Text(
                "➕ Create Job"
            )
        }

        Button(

            modifier =
                Modifier.fillMaxWidth(),

            onClick = {

            }

        ) {

            Text(
                "📋 My Jobs"
            )
        }

        Button(

            modifier =
                Modifier.fillMaxWidth(),

            onClick = {

            }

        ) {

            Text(
                "⭐ Auto Recruitment"
            )
        }
    }
}