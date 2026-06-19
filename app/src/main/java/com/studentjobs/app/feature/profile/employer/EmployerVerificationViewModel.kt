package com.studentjobs.app.feature.profile.employer

import android.net.Uri
import android.util.Log
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
    private val db = FirebaseFirestore.getInstance()

    // ========================================
    // UI STATE
    // ========================================
    var uiState by mutableStateOf(EmployerVerificationUiState())
        private set

    // ========================================
    // INIT
    // ========================================
    init {
        loadExistingBusinessProfile()
        observeVerificationStatus()
    }

    // ========================================
    // LẤY DỮ LIỆU CŨ (TỪ PROFILE)
    // ========================================
    private fun loadExistingBusinessProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                // Lấy thông tin từ collection 'employers' (nơi lưu hồ sơ doanh nghiệp)
                val snapshot = db.collection("employers").document(uid).get().await()
                if (snapshot.exists()) {
                    uiState = uiState.copy(
                        businessName = snapshot.getString("businessName") ?: "",
                        businessCategory = snapshot.getString("businessCategory") ?: "",
                        businessAddressText = snapshot.getString("businessAddressText") ?: "",
                        businessDescription = snapshot.getString("businessDescription") ?: "",
                        businessLocationUrl = snapshot.getString("businessLocationUrl") ?: "",
                        businessLatitude = snapshot.getDouble("businessLatitude"),
                        businessLongitude = snapshot.getDouble("businessLongitude")
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_ERROR", "Không thể load profile: ${e.message}")
            }
        }
    }

    // ========================================
    // CÁC HÀM XỬ LÝ INPUT
    // ========================================
    fun onBusinessNameChange(value: String) {
        uiState = uiState.copy(businessName = value)
    }

    fun onBusinessCategoryChange(value: String) {
        uiState = uiState.copy(businessCategory = value)
    }

    fun onBusinessAddressChange(value: String) {
        uiState = uiState.copy(businessAddressText = value)
    }

    fun onBusinessDescriptionChange(value: String) {
        uiState = uiState.copy(businessDescription = value)
    }

    fun onGoogleMapsUrlChange(value: String) {
        uiState = uiState.copy(businessLocationUrl = value)
    }

    fun onBusinessLicenseUploaded(uri: Uri) {
        uiState = uiState.copy(businessLicenseUri = uri)
    }

    fun onStorefrontUploaded(uri: Uri) {
        uiState = uiState.copy(businessStoreFrontUri = uri)
    }

    fun setBusinessLocation(latitude: Double, longitude: Double) {
        uiState = uiState.copy(businessLatitude = latitude, businessLongitude = longitude)
    }

    // ========================================
    // SUBMIT VERIFICATION
    // ========================================
    fun submitVerification() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true, errorMessage = null)
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

                if (uiState.businessLatitude == null || uiState.businessLongitude == null) {
                    throw Exception("Vui lòng chọn vị trí doanh nghiệp trên bản đồ")
                }

                val licenseUri = uiState.businessLicenseUri
                    ?: throw Exception("Giấy phép kinh doanh là bắt buộc")
                val storefrontUri =
                    uiState.businessStoreFrontUri ?: throw Exception("Ảnh mặt tiền là bắt buộc")

                val storage = FirebaseStorage.getInstance()

                // Upload files
                val licenseRef = storage.reference.child("business_licenses/$uid.jpg")
                licenseRef.putFile(licenseUri).await()
                val businessLicenseUrl = licenseRef.downloadUrl.await().toString()

                val storefrontRef = storage.reference.child("storefronts/$uid.jpg")
                storefrontRef.putFile(storefrontUri).await()
                val storefrontUrl = storefrontRef.downloadUrl.await().toString()

                // Submit to Firestore
                userService.submitEmployerVerification(
                    uid = uid,
                    businessName = uiState.businessName,
                    businessCategory = uiState.businessCategory,
                    businessAddress = uiState.businessAddressText,
                    businessDescription = uiState.businessDescription,
                    businessLocationUrl = uiState.businessLocationUrl,
                    businessLicenseUrl = businessLicenseUrl,
                    businessLatitude = uiState.businessLatitude!!,
                    businessLongitude = uiState.businessLongitude!!,
                    businessStoreFrontImageUrl = storefrontUrl
                )

                uiState = uiState.copy(
                    submissionStatus = VerificationStatus.PENDING,
                    verificationSubmitted = true,
                    isLoading = false
                )

            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.message)
                e.printStackTrace()
            }
        }
    }

    // ========================================
    // OBSERVE VERIFICATION
    // ========================================
    private fun observeVerificationStatus() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        verificationListener = db.collection("employer_verifications").document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    try {
                        uiState = uiState.copy(
                            businessLicenseVerified = VerificationStatus.valueOf(
                                snapshot.getString(
                                    "businessLicenseVerified"
                                ) ?: "UNVERIFIED"
                            ),
                            businessEmailVerified = VerificationStatus.valueOf(
                                snapshot.getString("businessEmailVerified") ?: "UNVERIFIED"
                            ),
                            businessPhoneVerified = VerificationStatus.valueOf(
                                snapshot.getString("businessPhoneVerified") ?: "UNVERIFIED"
                            ),
                            submissionStatus = VerificationStatus.valueOf(
                                snapshot.getString("submissionStatus") ?: "UNVERIFIED"
                            ),
                            verificationSubmitted = (snapshot.getString("submissionStatus") == "PENDING" || snapshot.getString(
                                "submissionStatus"
                            ) == "VERIFIED")
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
    }

    override fun onCleared() {
        verificationListener?.remove()
        super.onCleared()
    }
}