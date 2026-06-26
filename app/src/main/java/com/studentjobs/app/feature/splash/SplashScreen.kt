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
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(

    onNavigateLogin: () -> Unit,

    onNavigateMain: () -> Unit,

    onNavigateVerificationGate: () -> Unit

) {

    LaunchedEffect(Unit) {

        delay(1000)

        val currentUser =

            FirebaseAuth
                .getInstance()
                .currentUser

        if (currentUser == null) {

            onNavigateLogin()

            return@LaunchedEffect
        }

        val user =

            UserServiceNew()
                .getUserCore(currentUser.uid)

        if (user == null) {

            onNavigateLogin()

            return@LaunchedEffect
        }

        if (user.userVerified) {

            onNavigateMain()

        } else {

            onNavigateVerificationGate()
        }
    }

    Column(

        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        CircularProgressIndicator()

        Text("StudentJobs")
    }
}