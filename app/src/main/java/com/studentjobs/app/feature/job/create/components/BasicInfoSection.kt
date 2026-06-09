package com.studentjobs.app.feature.job.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicInfoSection(

    title: String,

    description: String,

    requiredApplicants: String,

    onTitleChange: (String) -> Unit,

    onDescriptionChange: (String) -> Unit,

    onApplicantsChange: (String) -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )

    ) {

        Column(

            modifier =
                Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            Text(

                text =
                    "📋 Job Information",

                style =
                    MaterialTheme.typography
                        .titleMedium
            )

            OutlinedTextField(

                value = title,

                onValueChange =
                    onTitleChange,

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Job Title"
                    )
                },

                placeholder = {

                    Text(
                        "Ex: Part-time Waiter"
                    )
                }
            )

            OutlinedTextField(

                value =
                    description,

                onValueChange =
                    onDescriptionChange,

                modifier =
                    Modifier.fillMaxWidth(),

                minLines = 4,

                label = {

                    Text(
                        "Job Description"
                    )
                },

                placeholder = {

                    Text(
                        "Describe the work, benefits and requirements..."
                    )
                }
            )

            OutlinedTextField(

                value =
                    requiredApplicants,

                onValueChange =
                    onApplicantsChange,

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Required Applicants"
                    )
                }
            )
        }
    }
}
