package com.studentjobs.app.feature.profile.employer

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EmployerVerificationViewModel : ViewModel() {

    // ========================================
    // FIREBASE
    // ========================================

    private var verificationListener: ListenerRegistration? = null

    private val userService = UserServiceNew()

    // ========================================
    // UI STATE
    // ========================================

    var uiState by mutableStateOf(
        EmployerVerificationUiState()
    )
        private set

    // ========================================
    // BUSINESS INFO
    // ========================================

    fun onBusinessNameChange(
        value: String
    ) {

        uiState = uiState.copy(

            businessName = value
        )
    }

    fun onBusinessCategoryChange(
        value: String
    ) {

        uiState = uiState.copy(

            businessCategory = value
        )
    }

    fun onBusinessAddressChange(
        value: String
    ) {

        uiState = uiState.copy(

            businessAddressText = value
        )
    }

    fun onBusinessDescriptionChange(
        value: String
    ) {

        uiState = uiState.copy(

            businessDescription = value
        )
    }

    fun onGoogleMapsUrlChange(
        value: String
    ) {

        uiState = uiState.copy(

            businessLocationUrl = value
        )
    }

    // ========================================
    // DOCUMENTS
    // ========================================

    fun onBusinessLicenseUploaded(
        uri: Uri
    ) {

        uiState = uiState.copy(

            businessLicenseUri = uri
        )
    }

    fun onStorefrontUploaded(
        uri: Uri
    ) {

        uiState = uiState.copy(

            businessStoreFrontUri = uri
        )
    }

    // ========================================
    // SUBMIT VERIFICATION
    // ========================================

    fun submitVerification() {

        viewModelScope.launch {

            try {

                uiState = uiState.copy(

                    isLoading = true,

                    errorMessage = null
                )

                val uid =

                    FirebaseAuth.getInstance().currentUser?.uid

                        ?: return@launch

                // ========================================
                // VALIDATION
                // ========================================

                val licenseUri =

                    uiState.businessLicenseUri

                        ?: throw Exception(
                            "Business license required"
                        )

                val storefrontUri =

                    uiState.businessStoreFrontUri

                        ?: throw Exception(
                            "Storefront image required"
                        )

                // ========================================
                // STORAGE
                // ========================================

                val storage = FirebaseStorage.getInstance()

                // ========================================
                // UPLOAD LICENSE
                // ========================================

                val licenseRef =

                    storage.reference.child(

                        "business_licenses/$uid.jpg"
                    )

                licenseRef.putFile(licenseUri).await()

                val businessLicenseUrl =

                    licenseRef.downloadUrl.await().toString()

                // ========================================
                // UPLOAD STOREFRONT
                // ========================================

                val storefrontRef =

                    storage.reference.child(

                        "storefronts/$uid.jpg"
                    )

                storefrontRef.putFile(storefrontUri).await()

                val storefrontUrl =

                    storefrontRef.downloadUrl.await().toString()

                // ========================================
                // SUBMIT
                // ========================================

                userService.submitEmployerVerification(

                    uid = uid,

                    businessName = uiState.businessName,

                    businessCategory = uiState.businessCategory,

                    businessAddress = uiState.businessAddressText,

                    businessDescription = uiState.businessDescription,

                    businessLocationUrl = uiState.businessLocationUrl,

                    businessLicenseUrl = businessLicenseUrl,

                    businessStoreFrontImageUrl = storefrontUrl
                )

                // ========================================
                // UPDATE UI
                // ========================================

                uiState = uiState.copy(

                    submissionStatus = VerificationStatus.PENDING,

                    verificationSubmitted = true,

                    isLoading = false
                )

            } catch (e: Exception) {

                uiState = uiState.copy(

                    isLoading = false,

                    errorMessage = e.message
                )

                e.printStackTrace()
            }
        }
    }

    // ========================================
    // INIT
    // ========================================

    init {

        observeVerificationStatus()
    }

    // ========================================
    // OBSERVE VERIFICATION
    // ========================================

    private fun observeVerificationStatus() {

        val uid =

            FirebaseAuth.getInstance().currentUser?.uid

                ?: return

        verificationListener =

            FirebaseFirestore.getInstance()

                .collection(
                    "employer_verifications"
                )

                .document(uid)

                .addSnapshotListener {

                        snapshot, _ ->

                    if (

                        snapshot != null &&

                        snapshot.exists()

                    ) {

                        try {

                            // =====================
                            // LICENSE STATUS
                            // =====================

                            val licenseStatus =

                                VerificationStatus.valueOf(

                                    snapshot.getString(

                                        "businessLicenseVerified"

                                    )

                                        ?: "UNVERIFIED"
                                )

                            // =====================
                            // EMAIL STATUS
                            // =====================

                            val emailStatus =

                                VerificationStatus.valueOf(

                                    snapshot.getString(

                                        "businessEmailVerified"

                                    )

                                        ?: "UNVERIFIED"
                                )

                            // =====================
                            // PHONE STATUS
                            // =====================

                            val phoneStatus =

                                VerificationStatus.valueOf(

                                    snapshot.getString(

                                        "businessPhoneVerified"

                                    )

                                        ?: "UNVERIFIED"
                                )

                            // =====================
                            // SUBMISSION STATUS
                            // =====================

                            val submissionStatus =

                                VerificationStatus.valueOf(

                                    snapshot.getString(

                                        "submissionStatus"

                                    )

                                        ?: "UNVERIFIED"
                                )

                            // =====================
                            // UPDATE UI
                            // =====================

                            uiState = uiState.copy(

                                businessLicenseVerified = licenseStatus,

                                businessEmailVerified = emailStatus,

                                businessPhoneVerified = phoneStatus,

                                submissionStatus = submissionStatus,

                                verificationSubmitted =

                                    submissionStatus ==

                                            VerificationStatus.PENDING

                                            ||

                                            submissionStatus ==

                                            VerificationStatus.VERIFIED
                            )

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }
                }
    }

    // ========================================
    // CLEAR
    // ========================================

    override fun onCleared() {

        verificationListener?.remove()

        super.onCleared()
    }

    fun verifyAddress() {

        uiState = uiState.copy(

            businessLatitude = 10.8507,

            businessLongitude = 106.7712,

            addressVerified = true
        )
    }
}