package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun VerifiedEmployerProfile(
    state: ProfileUiState
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ===== HEADER =====

        Text(
            text =
                if (state.businessName.isBlank())
                    "Employer Profile"
                else
                    state.businessName,

            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Verified Business Account",
            style = MaterialTheme.typography.bodyLarge
        )

        // ===== BUSINESS INFO =====

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Business Information",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Business Name: ${state.businessName}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Category: ${state.businessCategory}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Address: ${state.businessAddress}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Description: ${state.businessDescription}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Google Maps: ${state.googleMapsUrl}")
            }
        }

        // ===== BUSINESS LICENSE =====

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Business License",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                AsyncImage(
                    model = state.businessLicenseUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // ===== STOREFRONT =====

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Storefront Image",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                AsyncImage(
                    model = state.storeFrontImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}