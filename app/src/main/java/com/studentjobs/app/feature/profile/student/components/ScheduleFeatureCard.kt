package com.studentjobs.app.feature.profile.student.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleFeatureCard(

    onClick: () -> Unit

) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                onClick()
            },

        colors = CardDefaults.cardColors(

            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(

                    imageVector =
                        Icons.Default.CalendarMonth,

                    contentDescription =
                        null
                )

                Column(

                    modifier =
                        Modifier.padding(
                            start = 12.dp
                        )
                ) {

                    Text(

                        text =
                            "Smart Timetable OCR",

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(

                        text =
                            "Upload timetable to detect shift conflicts automatically.",

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall
                    )
                }
            }

            Icon(

                imageVector =
                    Icons.Default.KeyboardArrowRight,

                contentDescription =
                    null
            )
        }
    }
}