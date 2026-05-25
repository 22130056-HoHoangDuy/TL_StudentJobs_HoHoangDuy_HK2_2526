package com.studentjobs.app.feature.schedule

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ScheduleUploadScreen(

    viewModel: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {

    val uiState by viewModel.uiState.collectAsState()

    val currentUser =
        FirebaseAuth
            .getInstance()
            .currentUser

    // ========================================
    // IMAGE PICKER
    // ========================================

    val launcher =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.GetContent()

        ) { uri: Uri? ->

            uri?.let {

                viewModel.selectImage(it)
            }
        }

    // ========================================
    // UI
    // ========================================

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Top
    ) {

        Text(

            text = "Upload Timetable",

            style =
                MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ========================================
        // BANNER
        // ========================================

        Card(

            modifier = Modifier.fillMaxWidth(),

            colors =
                CardDefaults.cardColors(

                    containerColor =
                        Color(0xFFEEF4FF)
                ),

            shape =
                RoundedCornerShape(20.dp)
        ) {

            Column(

                modifier = Modifier.padding(20.dp)
            ) {

                Text(

                    text =
                        "Upload your timetable to activate Smart Auto Apply",

                    color = Color.Black
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(

                    text =
                        "The system will detect schedule conflicts automatically.",

                    color =
                        Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ========================================
        // IMAGE PREVIEW
        // ========================================

        uiState.selectedImageUri?.let { imageUri ->

            Image(

                painter =
                    rememberAsyncImagePainter(imageUri),

                contentDescription = null,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    ),

                contentScale =
                    ContentScale.Crop
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )
        }

        // ========================================
        // PICK IMAGE
        // ========================================

        Button(

            onClick = {

                launcher.launch("image/*")
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors =
                ButtonDefaults.buttonColors(

                    containerColor =
                        Color(0xFF2962FF)
                )
        ) {

            Text(
                text = "Choose Timetable Image"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ========================================
        // UPLOAD
        // ========================================

        Button(

            onClick = {

                currentUser?.uid?.let {

                    viewModel.uploadTimetable(it)
                }
            },

            enabled =
                uiState.selectedImageUri != null
                        &&
                        !uiState.isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors =
                ButtonDefaults.buttonColors(

                    containerColor =
                        Color(0xFFFFB300)
                )
        ) {

            if (uiState.isLoading) {

                CircularProgressIndicator(

                    color = Color.White
                )

            } else {

                Text(
                    text = "Upload Timetable"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ========================================
        // SUCCESS
        // ========================================

        uiState.successMessage?.let {

            Text(

                text = it,

                color =
                    Color(0xFF2E7D32)
            )
        }

        // ========================================
        // ERROR
        // ========================================

        uiState.errorMessage?.let {

            Text(

                text = it,

                color = Color.Red
            )
        }
    }
}