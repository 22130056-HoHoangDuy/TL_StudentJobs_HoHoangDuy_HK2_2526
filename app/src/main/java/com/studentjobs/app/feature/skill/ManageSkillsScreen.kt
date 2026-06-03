package com.studentjobs.app.feature.skill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.skill.BusinessCategory
import com.studentjobs.app.data.model.skill.SkillCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSkillsScreen(
    currentCategories: List<String>,

    currentSkills: List<String>,

    isPlus: Boolean,

    onSave: (

        List<String>, List<String>

    ) -> Unit


) {

    var selectedCategories by remember {

        mutableStateOf(
            currentCategories.toMutableList()
        )
    }

    var selectedSkills by remember {

        mutableStateOf(
            currentSkills.toMutableList()
        )
    }

    val maxCategories =

        if (isPlus) 5 else 2

    val maxSkills = 10

    val availableSkills =

        selectedCategories.flatMap {

            SkillCatalog.getSkillsByCategory(it)
        }.distinctBy {
            it.skillName
        }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Manage Skills"
                    )
                })
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                )

        ) {

            // ==================================
            // CATEGORY
            // ==================================

            Text(

                text = "Job Categories",

                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${selectedCategories.size}/$maxCategories selected"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            FlowRow(

                horizontalArrangement = Arrangement.spacedBy(8.dp),

                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                BusinessCategory.all.forEach { category ->

                    val selected =

                        selectedCategories.contains(category)

                    FilterChip(

                        selected = selected,

                        onClick = {

                            if (selected) {

                                selectedCategories =

                                    selectedCategories.toMutableList().apply {
                                        remove(category)
                                    }

                            } else {

                                if (

                                    selectedCategories.size

                                    < maxCategories

                                ) {

                                    selectedCategories =

                                        selectedCategories.toMutableList().apply {
                                            add(category)
                                        }
                                }
                            }
                        },

                        label = {

                            Text(category)
                        })
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // ==================================
            // SKILLS
            // ==================================

            Text(

                text = "Skills",

                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${selectedSkills.size}/$maxSkills selected"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            FlowRow(

                horizontalArrangement = Arrangement.spacedBy(8.dp),

                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                availableSkills.forEach { skill ->

                    val selected =

                        selectedSkills.contains(
                            skill.skillName
                        )

                    FilterChip(

                        selected = selected,

                        onClick = {

                            if (selected) {

                                selectedSkills =

                                    selectedSkills.toMutableList().apply {

                                        remove(
                                            skill.skillName
                                        )
                                    }

                            } else {

                                if (

                                    selectedSkills.size

                                    < maxSkills

                                ) {

                                    selectedSkills =

                                        selectedSkills.toMutableList().apply {

                                            add(
                                                skill.skillName
                                            )
                                        }
                                }
                            }
                        },

                        label = {

                            Text(
                                skill.skillName
                            )
                        })
                }
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(

                modifier = Modifier.fillMaxWidth(),

                enabled = selectedCategories.isNotEmpty(),

                onClick = {

                    onSave(

                        selectedCategories,

                        selectedSkills
                    )
                }

            ) {

                Text("Save")
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )
        }
    }
}
