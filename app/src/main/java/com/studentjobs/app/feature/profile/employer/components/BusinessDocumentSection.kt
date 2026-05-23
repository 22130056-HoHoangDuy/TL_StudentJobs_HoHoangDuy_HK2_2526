package com.studentjobs.app.feature.profile.employer.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.shared.components.UploadCard

@Composable
fun BusinessDocumentSection(

    enabled: Boolean = true,

    businessLicenseUri: Uri?,

    storefrontUri: Uri?,

    onUploadBusinessLicense: () -> Unit,

    onUploadStorefront: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(text = "Business Documents")

        UploadCard(
            title = "Business License",
            imageUri = businessLicenseUri,
            onClick = onUploadBusinessLicense,
            enabled = enabled

        )

        UploadCard(
            title = "Storefront Image",
            imageUri = storefrontUri,
            onClick = onUploadStorefront,
            enabled = enabled
        )
    }
}