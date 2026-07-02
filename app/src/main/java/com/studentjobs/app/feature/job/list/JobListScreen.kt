package com.studentjobs.app.feature.job.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.recommendation.DistanceUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    viewModel: JobListViewModel,
    onJobClick: (String) -> Unit,
    onMyJobsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showFilter by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Khám phá cơ hội 🚀",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Search + Filter
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.searchText,
                onValueChange = { viewModel.updateSearchText(it) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Tìm việc gì?") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilledIconButton(
                onClick = { showFilter = true },
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.FilterList, "Lọc")
            }
        }

        // VIP Section
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val gold = Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA500)))
            VIPButton(
                Modifier.weight(1f),
                "Đề xuất",
                Icons.Default.AutoAwesome,
                gold
            ) { viewModel.toggleSuggestedJobs() }
            VIPButton(
                Modifier.weight(1f),
                "Ứng tuyển thông minh",
                Icons.Default.FlashOn,
                gold
            ) { viewModel.toggleAutoApply() }
        }

        LazyColumn(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(state.jobs) { job ->

                val studentLat =
                    state.studentLatitude

                val studentLng =
                    state.studentLongitude

                val jobLat =
                    job.latitude

                val jobLng =
                    job.longitude

                val distanceKm =

                    if (

                        studentLat != null &&
                        studentLng != null &&
                        jobLat != null &&
                        jobLng != null

                    ) {
                        DistanceUtils
                            .calculateDistanceKm(

                                studentLat,
                                studentLng,

                                jobLat,
                                jobLng
                            )

                    } else {

                        null
                    }

                JobCard(

                    job = job,

                    distanceKm = distanceKm,

                    onClick = {

                        onJobClick(
                            job.jobId
                        )
                    }
                )
            }
        }
    }

    if (showFilter) {
        FilterBottomSheet(
            viewModel = viewModel,
            state = state,
            onDismiss = { showFilter = false },
            onApply = { dist, sal, sk, cat ->
                viewModel.applyFilters(dist, sal, sk, cat)
                showFilter = false
            }
        )
    }
}

@Composable
fun VIPButton(
    m: Modifier,
    txt: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    b: Brush,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = m
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp)),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.background(b),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = Color.Black)
            Spacer(Modifier.width(8.dp))
            Text(txt, color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    viewModel: JobListViewModel,
    state: JobListUiState,
    onDismiss: () -> Unit,
    onApply: (Float, Double, List<String>, List<String>) -> Unit
) {
    val allCategories by viewModel.categories.collectAsState()
    var d by remember { mutableStateOf(state.filterDistance) }
    var s by remember { mutableStateOf(state.minSalary) }
    val selectedSkills = remember { mutableStateListOf(*state.selectedSkills.toTypedArray()) }
    val selectedCats = remember { mutableStateListOf<String>() }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Text(
                "Lọc công việc",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text("Khoảng cách: ${d.toInt()} km"); Slider(
            value = d,
            onValueChange = { d = it },
            valueRange = 1f..50f
        )
            Text("Lương tối thiểu: ${s.toInt()}k/h"); Slider(
            value = s.toFloat(),
            onValueChange = { s = it.toDouble() },
            valueRange = 0f..200f
        )

            Text(
                "Ngành nghề:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            FlowRow {
                allCategories.forEach { cat ->
                    FilterChip(
                        selected = selectedCats.contains(cat),
                        onClick = {
                            if (selectedCats.contains(cat)) selectedCats.remove(cat) else selectedCats.add(
                                cat
                            )
                        },
                        label = { Text(cat) }
                    )
                }
            }

            Button(
                onClick = { onApply(d, s, selectedSkills.toList(), selectedCats.toList()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Áp dụng bộ lọc")
            }
        }
    }
}