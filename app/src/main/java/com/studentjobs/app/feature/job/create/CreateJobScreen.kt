package com.studentjobs.app.feature.job.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.feature.job.create.components.BasicInfoSection
import com.studentjobs.app.feature.job.create.components.JobPreviewCard
import com.studentjobs.app.feature.job.create.components.SalarySection
import com.studentjobs.app.feature.job.create.components.ShiftSection
import com.studentjobs.app.feature.job.create.components.SkillSelectorSection

@Composable
fun CreateJobScreen(

    employerBusinessName: String,

    viewModel: CreateJobViewModel,

    onNavigateToSubscription: () -> Unit

) {

    val state by
    viewModel.uiState.collectAsState()

    val auth =
        FirebaseAuth.getInstance()

    val employerUid =
        auth.currentUser?.uid

    LazyColumn(

        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)

    ) {

        // ====================================
        // PREVIEW
        // ====================================

        item {

            JobPreviewCard(

                title =
                    state.title,

                salaryMin =
                    state.salaryMin,

                salaryMax =
                    state.salaryMax,

                businessName =
                    employerBusinessName,

                selectedSkillCount =
                    state.selectedSkills.size
            )
        }

        // ====================================
        // BASIC INFO
        // ====================================

        item {

            BasicInfoSection(

                title =
                    state.title,

                description =
                    state.description,

                requiredApplicants =
                    state.requiredApplicants,

                onTitleChange =
                    viewModel::updateTitle,

                onDescriptionChange =
                    viewModel::updateDescription,

                onApplicantsChange =
                    viewModel::updateRequiredApplicants
            )
        }

        // ====================================
        // SALARY
        // ====================================

        item {

            SalarySection(

                salaryMin =
                    state.salaryMin,

                salaryMax =
                    state.salaryMax,

                onSalaryMinChange =
                    viewModel::updateSalaryMin,

                onSalaryMaxChange =
                    viewModel::updateSalaryMax
            )
        }

        // ====================================
        // SKILLS
        // ====================================

        item {

            SkillSelectorSection(

                availableSkills =
                    state.availableSkills,

                selectedSkills =
                    state.selectedSkills,

                onSkillToggle =
                    viewModel::toggleSkill
            )
        }

        // ====================================
        // SHIFTS
        // ====================================

        item {

            ShiftSection(

                shifts =
                    state.shifts,

                selectedDay =
                    state.selectedDay,

                startMinute =
                    state.startMinute,

                endMinute =
                    state.endMinute,

                slots =
                    state.slots,

                onDaySelected =
                    viewModel::updateSelectedDay,

                onStartMinuteChange =
                    viewModel::updateStartMinute,

                onEndMinuteChange =
                    viewModel::updateEndMinute,

                onAddShift =
                    viewModel::addShift,

                onDeleteShift =
                    viewModel::removeShift
            )
        }

        // ====================================
        // AUTO RECRUITMENT
        // ====================================

        item {

            Card {

                Column(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    Text(
                        "✨ Smart Auto Recruitment"
                    )

                    Text(
                        "Automatically recruit the most suitable students."
                    )

                    if (
                        state.isPlusEmployer
                    ) {

                        Row(

                            horizontalArrangement =
                                Arrangement.SpaceBetween,

                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text(
                                "Enable Auto Recruitment"
                            )

                            Switch(

                                checked =
                                    state.autoRecruitmentEnabled,

                                onCheckedChange = {

                                    viewModel
                                        .toggleAutoRecruitment()
                                }
                            )
                        }

                    } else {

                        Text(
                            "🔒 Available for PLUS members"
                        )

                        Button(

                            onClick =
                                onNavigateToSubscription

                        ) {

                            Text(
                                "Upgrade to PLUS"
                            )
                        }
                    }
                }
            }
        }

        // ====================================
        // ERROR
        // ====================================

        state.errorMessage?.let {

            item {

                Text(

                    text = it,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }

        // ====================================
        // SUCCESS
        // ====================================

        if (state.success) {

            item {

                Text(

                    text =
                        "✅ Job created successfully",

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }
        }

        // ====================================
        // PUBLISH
        // ====================================

        item {

            Button(

                onClick = {

                    employerUid?.let {

                        viewModel
                            .createJob(it)
                    }
                },

                modifier =
                    Modifier.fillMaxWidth(),

                enabled =
                    !state.isLoading

            ) {

                if (
                    state.isLoading
                ) {

                    CircularProgressIndicator()

                } else {

                    Text(
                        "🚀 Publish Job"
                    )
                }
            }
        }
    }
}