package com.studentjobs.app.feature.job.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JobListScreen(
    viewModel: JobListViewModel,
    onJobClick: (String) -> Unit,
    onMyJobsClick: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    val filteredJobs =

        state.jobs.filter {

            it.title.contains(

                state.searchText,

                ignoreCase = true
            )
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        OutlinedTextField(

            value = state.searchText,

            onValueChange = {

                viewModel.updateSearchText(it)
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {

                Text(
                    "Tìm kiếm công việc"
                )
            }
        )
        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Button(

            onClick =
                onMyJobsClick,

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Text(
                "Công việc của tôi"
            )
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        if (state.isLoading) {

            CircularProgressIndicator()

            return@Column
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        if (filteredJobs.isEmpty()) {

            Text(

                text = "Không tìm thấy công việc nào",

                modifier =
                    Modifier.padding(
                        top = 24.dp
                    )
            )

            return@Column
        }

        LazyColumn(

            contentPadding =
                PaddingValues(
                    vertical = 16.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )

        ) {

            items(
                filteredJobs
            ) { job ->

                JobCard(

                    job = job,

                    onClick = {

                        onJobClick(job.jobId)
                    }
                )
            }
        }
    }
}

