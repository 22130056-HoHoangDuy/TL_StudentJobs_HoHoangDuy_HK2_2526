package com.studentjobs.app.ui.screen.role

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.ui.component.RoleCard

@Composable
fun RoleSelectionScreen(
    onContinue: (UserRole) -> Unit
) {

    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF3B82F6), Color(0xFF60A5FA))
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Who are you?",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            RoleCard(
                title = "Student",
                description = "Find part-time jobs",
                selected = selectedRole == UserRole.STUDENT,
                icon = { Icon(Icons.Default.School, contentDescription = null) }
            ) {
                selectedRole = UserRole.STUDENT
            }

            Spacer(modifier = Modifier.height(16.dp))

            RoleCard(
                title = "Employer",
                description = "Post jobs & hire students",
                selected = selectedRole == UserRole.EMPLOYER,
                icon = { Icon(Icons.Default.Business, contentDescription = null) }
            ) {
                selectedRole = UserRole.EMPLOYER
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    selectedRole?.let { onContinue(it) }
                },
                enabled = selectedRole != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Continue")
            }
        }
    }
}