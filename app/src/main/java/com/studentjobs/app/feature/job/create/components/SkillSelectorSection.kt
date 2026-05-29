package com.studentjobs.app.feature.job.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkillSelectorSection(

    availableSkills: List<String>,

    selectedSkills: List<String>,

    onSkillToggle: (String) -> Unit

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
                    "🎯 Required Skills",

                style =
                    MaterialTheme.typography
                        .titleMedium
            )

            Text(

                text =
                    "Select the skills required for this job"
            )

            FlowRow(

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )

            ) {

                availableSkills.forEach { skill ->

                    val selected =

                        selectedSkills
                            .contains(skill)

                    FilterChip(

                        selected =
                            selected,

                        onClick = {

                            onSkillToggle(
                                skill
                            )
                        },

                        label = {

                            Text(
                                getSkillLabel(
                                    skill
                                )
                            )
                        },

                        colors =
                            FilterChipDefaults
                                .filterChipColors()
                    )
                }
            }
        }
    }
}