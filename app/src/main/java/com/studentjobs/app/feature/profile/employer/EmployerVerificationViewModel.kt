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
import com.studentjobs.app.firebase.firestore.UserService
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EmployerVerificationViewModel : ViewModel() {
    private var verificationListener: ListenerRegistration? = null
    private val userService = UserService()
    private var userListener: ListenerRegistration? = null

    var uiState by mutableStateOf(EmployerVerificationUiState())
        private set

    // ===== BUSINESS INFO =====

    fun onBusinessNameChange(value: String) {
        uiState = uiState.copy(
            businessName = value
        )
    }

    fun onBusinessCategoryChange(value: String) {
        uiState = uiState.copy(
            businessCategory = value
        )
    }

    fun onBusinessAddressChange(value: String) {
        uiState = uiState.copy(
            businessAddress = value
        )
    }

    fun onBusinessDescriptionChange(value: String) {
        uiState = uiState.copy(
            businessDescription = value
        )
    }

    fun onGoogleMapsUrlChange(value: String) {
        uiState = uiState.copy(
            googleMapsUrl = value
        )
    }

    // ===== DOCUMENTS =====

    fun onBusinessLicenseUploaded(uri: Uri) {
        uiState = uiState.copy(
            businessLicenseUri = uri
        )
    }

    fun onStorefrontUploaded(uri: Uri) {
        uiState = uiState.copy(
            storeFrontUri = uri
        )
    }

    // ===== STORAGE =====

    private suspend fun uploadImage(
        uri: Uri, folder: String
    ): String {

        val uid =
            FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User not logged in")

        val ref =
            FirebaseStorage.getInstance().reference.child("employer_verification/$uid/$folder")

        ref.putFile(uri).await()

        return ref.downloadUrl.await().toString()
    }

    // ===== SUBMIT =====

    fun submitVerification() {

        viewModelScope.launch {

            try {

                uiState = uiState.copy(
                    isLoading = true, errorMessage = null
                )

                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

                // ===== STORAGE =====

                val storage = FirebaseStorage.getInstance()

                // ===== UPLOAD LICENSE =====

                val licenseUri =
                    uiState.businessLicenseUri ?: throw Exception("Business license required")

                val licenseRef = storage.reference.child("business_licenses/$uid.jpg")

                licenseRef.putFile(licenseUri).await()

                val businessLicenseUrl = licenseRef.downloadUrl.await().toString()

                // ===== UPLOAD STOREFRONT =====

                val storefrontUri =
                    uiState.storeFrontUri ?: throw Exception("Storefront image required")

                val storefrontRef = storage.reference.child("storefronts/$uid.jpg")

                storefrontRef.putFile(storefrontUri).await()

                val storefrontUrl = storefrontRef.downloadUrl.await().toString()
                //
                userService.submitEmployerVerification(
                    uid = uid,

                    businessName = uiState.businessName,

                    businessCategory = uiState.businessCategory,

                    businessAddress = uiState.businessAddress,

                    businessDescription = uiState.businessDescription,

                    googleMapsUrl = uiState.googleMapsUrl,

                    businessLicenseUrl = businessLicenseUrl,

                    storeFrontImageUrl = storefrontUrl
                )

                uiState = uiState.copy(
                    verificationStatus = "PENDING", isLoading = false
                )

            } catch (e: Exception) {

                uiState = uiState.copy(
                    isLoading = false, errorMessage = e.message
                )

                e.printStackTrace()
            }
        }
    }

    init {

        observeVerificationStatus()

        observeUserVerification()
    }

    private fun observeVerificationStatus() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        verificationListener =
            FirebaseFirestore.getInstance().collection("employer_verifications").document(uid)
                .addSnapshotListener { snapshot, _ ->

                    if (snapshot != null && snapshot.exists()) {

                        val status = snapshot.getString("status")

                        uiState = uiState.copy(
                            verificationStatus = status ?: "PENDING", verificationSubmitted = true
                        )
                    }
                }
    }

    private fun observeUserVerification() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        userListener = FirebaseFirestore.getInstance().collection("users").document(uid)
            .addSnapshotListener { snapshot, _ ->

                if (snapshot != null && snapshot.exists()) {

                    uiState = uiState.copy(

                        isEmailVerified = snapshot.getBoolean("isEmailVerified") ?: false,

                        isPhoneVerified = snapshot.getBoolean("isPhoneVerified") ?: false
                    )
                }
            }
    }


    // Not leak memory
    override fun onCleared() {
        userListener?.remove()
    }
}