package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.skill.BusinessCategory
import com.studentjobs.app.feature.profile.employer.EmployerVerificationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessInfoSection(

    enabled: Boolean = true,

    state: EmployerVerificationUiState,

    onBusinessNameChange: (String) -> Unit,

    onBusinessCategoryChange: (String) -> Unit,

    onBusinessAddressChange: (String) -> Unit,

    onBusinessDescriptionChange: (String) -> Unit,

    onGoogleMapsUrlChange: (String) -> Unit,

    onSelectLocation: () -> Unit
) {

    var expanded by remember {

        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            Text(
                text = "Business Information"
            )

            // ====================================
            // BUSINESS NAME
            // ====================================

            OutlinedTextField(

                value = state.businessName,

                onValueChange =
                    onBusinessNameChange,

                label = {
                    Text("Business Name")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                enabled = enabled
            )

            // ====================================
            // BUSINESS CATEGORY
            // ====================================

            ExposedDropdownMenuBox(

                expanded = expanded,

                onExpandedChange = {

                    expanded = !expanded
                }
            ) {

                OutlinedTextField(

                    value =
                        state.businessCategory,

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text(
                            "Business Category"
                        )
                    },

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded = expanded
                            )
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                )

                DropdownMenu(

                    expanded = expanded,

                    onDismissRequest = {

                        expanded = false
                    }

                ) {

                    BusinessCategory.all.forEach {

                            category ->

                        DropdownMenuItem(

                            text = {

                                Text(category)
                            },

                            onClick = {

                                onBusinessCategoryChange(
                                    category
                                )

                                expanded = false
                            }
                        )
                    }
                }
            }

            // ====================================
            // ADDRESS
            // ====================================

            OutlinedTextField(

                value =
                    state.businessAddressText,

                onValueChange =
                    onBusinessAddressChange,

                label = {
                    Text("Business Address")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                enabled = enabled
            )

            Button(

                onClick = onSelectLocation,
            ) {

                Text(
                    "Select Business Location"
                )
            }

            if (

                state.businessLatitude != null &&

                state.businessLongitude != null

            ) {

                Text(
                    text =
                        "📍 ${state.businessLatitude}, ${state.businessLongitude}"
                )
            }

            // ====================================
            // GOOGLE MAP URL
            // ====================================

            OutlinedTextField(

                value =
                    state.businessLocationUrl,

                onValueChange =
                    onGoogleMapsUrlChange,

                label = {
                    Text(
                        "Google Maps URL"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                enabled = enabled
            )

            // ====================================
            // DESCRIPTION
            // ====================================

            OutlinedTextField(

                value =
                    state.businessDescription,

                onValueChange =
                    onBusinessDescriptionChange,

                label = {
                    Text(
                        "Business Description"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                enabled = enabled,

                minLines = 4
            )
        }
    }
}