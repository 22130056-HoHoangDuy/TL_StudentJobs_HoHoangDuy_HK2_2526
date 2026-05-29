package com.studentjobs.app.feature.job.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SalarySection(

    salaryMin: String,

    salaryMax: String,

    onSalaryMinChange: (String) -> Unit,

    onSalaryMaxChange: (String) -> Unit

) {

    val quickValues = listOf(

        "25000",

        "30000",

        "35000",

        "50000"
    )

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
                    "💰 Salary",

                style =
                    MaterialTheme.typography
                        .titleMedium
            )

            Text(

                text =
                    "Quick Select"
            )

            FlowRow(

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )

            ) {

                quickValues.forEach {

                    AssistChip(

                        onClick = {

                            onSalaryMinChange(
                                it
                            )

                            onSalaryMaxChange(
                                it
                            )
                        },

                        label = {

                            Text(
                                "${it}đ"
                            )
                        }
                    )
                }
            }

            OutlinedTextField(

                value = salaryMin,

                onValueChange =
                    onSalaryMinChange,

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Minimum Salary"
                    )
                }
            )

            OutlinedTextField(

                value = salaryMax,

                onValueChange =
                    onSalaryMaxChange,

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Maximum Salary"
                    )
                }
            )
        }
    }
}