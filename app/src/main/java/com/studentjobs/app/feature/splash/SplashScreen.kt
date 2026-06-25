package com.studentjobs.app.feature.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(

    onNavigateLogin: () -> Unit,

    onNavigateMain: (Boolean) -> Unit

) {

    LaunchedEffect(Unit) {

        delay(1000)

        val currentUser =
            FirebaseAuth
                .getInstance()
                .currentUser

        if (currentUser == null) {

            onNavigateLogin()

        } else {

            // Tạm thời chưa kiểm tra userVerified
            onNavigateMain(false)
        }
    }

    Column(

        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        CircularProgressIndicator()

        Text(
            text = "StudentJobs"
        )
    }
}