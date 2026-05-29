package com.studentjobs.app.feature.job.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.job.create.components.BasicInfoSection
import com.studentjobs.app.feature.job.create.components.JobPreviewCard
import com.studentjobs.app.feature.job.create.components.SalarySection
import com.studentjobs.app.feature.job.create.components.ShiftSection
import com.studentjobs.app.feature.job.create.components.SkillSelectorSection

@Composable
fun CreateJobScreen(

    employerBusinessName: String,

    viewModel: CreateJobViewModel

) {

    val state by
    viewModel.uiState.collectAsState()

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

                onSlotsChange =
                    viewModel::updateSlots,

                onAddShift =
                    viewModel::addShift,

                onDeleteShift =
                    viewModel::removeShift
            )
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
        // PUBLISH
        // ====================================

        item {

            Button(

                onClick = {

                    // TODO:
                    // truyền employerUid
                },

                modifier =
                    Modifier.fillMaxWidth()

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