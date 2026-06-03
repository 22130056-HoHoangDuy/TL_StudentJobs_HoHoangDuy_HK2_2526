package com.studentjobs.app.feature.job.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JobPreviewCard(

    title: String,

    salaryMin: String,

    salaryMax: String,

    businessName: String,

    selectedSkillCount: Int

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )

    ) {

        Column(

            modifier = Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)

        ) {

            Text(

                text = "👀 Job Preview",

                style =
                    MaterialTheme.typography
                        .titleMedium
            )

            Text(

                text =
                    if (title.isBlank())

                        "Untitled Job"
                    else

                        title,

                style =
                    MaterialTheme.typography
                        .headlineSmall
            )

            Row(

                horizontalArrangement =
                    Arrangement.spacedBy(
                        6.dp
                    )

            ) {

                Icon(

                    imageVector =
                        Icons.Default.Work,

                    contentDescription =
                        null
                )

                Text(

                    text =

                        if (

                            salaryMin.isNotBlank()
                            &&
                            salaryMax.isNotBlank()

                        ) {

                            "$salaryMin - $salaryMax VND/hour"

                        } else {

                            "Salary not specified"
                        }
                )
            }

            Row(

                horizontalArrangement =
                    Arrangement.spacedBy(
                        6.dp
                    )

            ) {

                Icon(

                    imageVector =
                        Icons.Default.LocationOn,

                    contentDescription =
                        null
                )

                Text(
                    text = businessName
                )
            }

            Text(

                text =
                    "🎯 Skills Selected: $selectedSkillCount"
            )
        }
    }
}