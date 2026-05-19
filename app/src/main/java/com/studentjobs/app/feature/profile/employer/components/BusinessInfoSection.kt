package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.employer.EmployerVerificationUiState

@Composable
fun BusinessInfoSection(
    enabled: Boolean = true,

    state: EmployerVerificationUiState,

    onBusinessNameChange: (String) -> Unit,

    onBusinessCategoryChange: (String) -> Unit,

    onBusinessAddressChange: (String) -> Unit,

    onBusinessDescriptionChange: (String) -> Unit,

    onGoogleMapsUrlChange: (String) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(text = "Business Information")

            OutlinedTextField(
                value = state.businessName,
                onValueChange = onBusinessNameChange,
                label = { Text("Business Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )

            OutlinedTextField(
                value = state.businessCategory,
                onValueChange = onBusinessCategoryChange,
                label = { Text("Business Category") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )

            OutlinedTextField(
                value = state.businessAddress,
                onValueChange = onBusinessAddressChange,
                label = { Text("Business Address") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )

            OutlinedTextField(
                value = state.googleMapsUrl,
                onValueChange = onGoogleMapsUrlChange,
                label = { Text("Google Maps URL") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )

            OutlinedTextField(
                value = state.businessDescription,
                onValueChange = onBusinessDescriptionChange,
                label = { Text("Business Description") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )
        }
    }
}