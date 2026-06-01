package com.studentjobs.app.feature.profile.student.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun LocationInfoCard(

    state: ProfileUiState,

    onSelectLocation: () -> Unit,

    onOpenGoogleMaps: () -> Unit = {}

) {

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E1B4B)
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .background(gradient)
                .padding(22.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF06B6D4)
                )

                Text(
                    text = " Location Information",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            val profile = state.studentProfile

            if (

                profile?.studentLatitude == null ||

                profile.studentLongitude == null

            ) {

                Text(
                    text = "Location not selected",
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = onSelectLocation
                ) {

                    Text(
                        "Select Location"
                    )
                }

            } else {

                Text(
                    text = "Current Location",
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Lat: ${profile.studentLatitude}",
                    color = Color.White
                )

                Text(
                    text =
                        "Lng: ${profile.studentLongitude}",
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row {

                    Button(
                        onClick = onSelectLocation
                    ) {

                        Text(
                            "Change Location"
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    if (
                        !profile.studentLocationUrl.isNullOrBlank()
                    ) {

                        TextButton(
                            onClick = onOpenGoogleMaps
                        ) {

                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(4.dp)
                            )

                            Text(
                                "Maps"
                            )
                        }
                    }
                }
            }
        }
    }
}