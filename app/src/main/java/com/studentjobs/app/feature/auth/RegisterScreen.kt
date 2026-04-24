package com.studentjobs.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.utils.UiState
import com.studentjobs.app.utils.AppPreferences

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: (User) -> Unit
) {

    val context = LocalContext.current

    // ✅ convert String → UserRole an toàn
    val role = remember {
        AppPreferences(context)
            .getUserRole()
            ?.let { UserRole.valueOf(it) }
    }

    val state by viewModel.registerState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF3B82F6), Color(0xFF60A5FA))
                )
            )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Create Account 🚀",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (role != null) {
                            viewModel.register(email, password, role)
                        }
                    },
                    enabled = role != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Register")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val s = state) {
                    is UiState.Loading -> CircularProgressIndicator()

                    is UiState.Error -> Text(
                        s.message,
                        color = MaterialTheme.colorScheme.error
                    )

                    is UiState.Success<*> -> {
                        val user = s.data as User
                        LaunchedEffect(user.uid) {
                            onRegisterSuccess(user)
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}