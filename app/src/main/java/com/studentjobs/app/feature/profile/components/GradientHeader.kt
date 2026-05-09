package com.studentjobs.app.feature.profile.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.studentjobs.app.feature.profile.ProfileUiState
import com.studentjobs.app.firebase.firestore.UserService

@Composable
fun GradientHeader(
    state: ProfileUiState
) {

    val context = LocalContext.current

    val userService = remember {
        UserService()
    }

    val selectedImage = remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null) {

            selectedImage.value = uri

            userService.uploadAvatar(imageUri = uri, onSuccess = {

                Toast.makeText(
                    context, "Avatar updated", Toast.LENGTH_SHORT
                ).show()
            }, onError = {

                Toast.makeText(
                    context, it, Toast.LENGTH_SHORT
                ).show()
            })
        }
    }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2563EB), Color(0xFF7C3AED), Color(0xFF06B6D4)
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .background(gradient)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                contentAlignment = Alignment.BottomEnd
            ) {

                Image(
                    painter = rememberAsyncImagePainter(
                        model = if (state.avatarUrl.isNotEmpty()) {
                            state.avatarUrl
                        } else {
                            "https://i.imgur.com/tGbaZCY.jpg"
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 4.dp, color = Color.White, shape = CircleShape
                        )
                        .clickable {
                            launcher.launch("image/*")
                        },
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            launcher.launch("image/*")
                        }, contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (state.extractedName.isNotEmpty()) {
                    state.extractedName
                } else {
                    state.name
                },
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80)
                )

                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = "Verified Student",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.school, color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.18f))
                    .padding(
                        horizontal = 24.dp, vertical = 12.dp
                    )
            ) {

                Text(
                    text = "Trust Score: ${state.trustScore}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}