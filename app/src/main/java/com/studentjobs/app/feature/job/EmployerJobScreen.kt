package com.studentjobs.app.feature.job

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun EmployerJobScreen(
    navController: NavController
) {

    Column {

        Button(
            onClick = {
                navController.navigate(
                    "create_job"
                )
            }
        ) {
            Text("Tạo công việc")
        }

        Text(
            "Danh sách công việc đã đăng"
        )
    }
}