package com.studentjobs.app.feature.profile.employer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.feature.profile.employer.components.BusinessDocumentSection
import com.studentjobs.app.feature.profile.employer.components.BusinessInfoSection
import com.studentjobs.app.feature.profile.shared.components.VerificationBanner
import com.studentjobs.app.feature.profile.shared.components.VerificationCard
import com.studentjobs.app.feature.profile.shared.components.VerificationStatus

@Composable
fun EmployerVerificationScreen(
    navController: NavController,
    viewModel: EmployerVerificationViewModel = viewModel()
) {

    val state = viewModel.uiState

    // ===== LICENSE PICKER =====

    val businessLicensePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            uri?.let {
                viewModel.onBusinessLicenseUploaded(it)
            }
        }

    // ===== STOREFRONT PICKER =====

    val storefrontPicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            uri?.let {
                viewModel.onStorefrontUploaded(it)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ===== TOP BANNER =====

        if (state.verificationSubmitted) {

            VerificationBanner(
                title = "Verification Submitted",
                subtitle = "Your business verification is under manual review"
            )

        } else {

            VerificationBanner(
                title = "Complete verification",
                subtitle = "Complete verification to increase your trust score"
            )
        }

        // ===== EMAIL =====

        VerificationCard(
            title = "Verify Email",

            description = "Confirm your business email",

            status = when {

                state.isEmailVerified ->
                    VerificationStatus.VERIFIED

                state.verificationStatus == "PENDING" ->
                    VerificationStatus.PENDING

                else ->
                    VerificationStatus.NOT_VERIFIED
            },

            enabled = !state.verificationSubmitted,

            onClick = {

                navController.navigate(
                    "email_verification/EMPLOYER"
                )
            }
        )

        // ===== PHONE =====

        VerificationCard(
            title = "Verify Phone Number",

            description = "Confirm your business phone number",

            status = when {

                state.isPhoneVerified ->
                    VerificationStatus.VERIFIED

                state.verificationStatus == "PENDING" ->
                    VerificationStatus.PENDING

                else ->
                    VerificationStatus.NOT_VERIFIED
            },

            enabled = !state.verificationSubmitted,

            onClick = {

                navController.navigate(
                    "phone_verification"
                )
            }
        )

        // ===== BUSINESS INFO =====

        BusinessInfoSection(

            enabled = !state.verificationSubmitted,

            state = state,

            onBusinessNameChange =
                viewModel::onBusinessNameChange,

            onBusinessCategoryChange =
                viewModel::onBusinessCategoryChange,

            onBusinessAddressChange =
                viewModel::onBusinessAddressChange,

            onBusinessDescriptionChange =
                viewModel::onBusinessDescriptionChange,

            onGoogleMapsUrlChange =
                viewModel::onGoogleMapsUrlChange
        )

        // ===== DOCUMENTS =====

        BusinessDocumentSection(

            enabled = !state.verificationSubmitted,

            businessLicenseUri =
                state.businessLicenseUri,

            storefrontUri =
                state.storeFrontUri,

            onUploadBusinessLicense = {

                businessLicensePicker.launch("image/*")
            },

            onUploadStorefront = {

                storefrontPicker.launch("image/*")
            }
        )

        // ===== SUBMIT =====

        Button(
            onClick = {

                viewModel.submitVerification()
            },

            enabled =
                !state.isLoading &&
                        !state.verificationSubmitted
        ) {

            Text(

                text =
                    if (state.isLoading)
                        "Submitting..."
                    else
                        "Submit Verification Request"
            )
        }
    }
}