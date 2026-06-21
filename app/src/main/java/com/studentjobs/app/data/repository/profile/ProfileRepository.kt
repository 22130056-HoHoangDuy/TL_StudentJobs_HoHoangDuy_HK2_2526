package com.studentjobs.app.data.repository.profile

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.studentjobs.app.data.model.employer.EmployerProfile
import com.studentjobs.app.data.model.employer.EmployerVerification
import com.studentjobs.app.data.model.student.StudentProfile
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.StudentService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import com.studentjobs.app.firebase.firestore.VerificationService
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val userService: UserServiceNew,
    private val studentService: StudentService,
    private val employerService: EmployerService,
    private val verificationService: VerificationService
) {

    // ========================================
    // GET USER CORE
    // ========================================
    suspend fun getUserCore(uid: String): UserCore? {
        return userService.getUserCore(uid)
    }

    // ========================================
    // STUDENT PROFILE
    // ========================================
    suspend fun getStudentProfile(uid: String): StudentProfile? {
        return studentService.getStudentProfile(uid)
    }

    suspend fun updateStudentProfile(profile: StudentProfile): Result<Unit> {
        return studentService.updateStudentProfile(profile)
    }

    // ========================================
    // STUDENT VERIFICATION
    // ========================================
    suspend fun getStudentVerification(uid: String): StudentVerification? {
        return verificationService.getStudentVerification(uid)
    }

    suspend fun updateStudentVerificationFields(
        uid: String,
        fields: Map<String, Any>
    ): Result<Unit> {
        return verificationService.updateStudentVerificationFields(uid = uid, fields = fields)
    }

    // ========================================
    // EMPLOYER PROFILE
    // ========================================
    suspend fun getEmployerProfile(uid: String): EmployerProfile? {
        return employerService.getEmployerProfile(uid)
    }

    // Hàm này đã có sẵn khớp với ViewModel của anh
    suspend fun updateEmployerProfile(uid: String, profile: EmployerProfile): Result<Unit> {
        return employerService.updateEmployerProfile(profile)
    }

    // ========================================
    // EMPLOYER VERIFICATION
    // ========================================
    suspend fun getEmployerVerification(uid: String): EmployerVerification? {
        return verificationService.getEmployerVerification(uid)
    }

    suspend fun updateEmployerVerificationFields(
        uid: String,
        fields: Map<String, Any>
    ): Result<Unit> {
        return verificationService.updateEmployerVerificationFields(uid = uid, fields = fields)
    }

    // ========================================
    // 🔥 FIREBASE STORAGE (THÊM MỚI ĐỂ UP AVATAR/LOGO)
    // ========================================
    suspend fun uploadEmployerStorageFile(path: String, uri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference.child(path)
        storageRef.putFile(uri).await()
        return storageRef.downloadUrl.await().toString()
    }

    // ========================================
    // CHECK ROLE & LISTENERS
    // ========================================
    suspend fun isStudent(uid: String): Boolean {
        val user = userService.getUserCore(uid)
        return user?.role == UserRole.STUDENT
    }

    suspend fun isEmployer(uid: String): Boolean {
        val user = userService.getUserCore(uid)
        return user?.role == UserRole.EMPLOYER
    }

    suspend fun updateStudentVerifiedStatus(uid: String, verified: Boolean): Result<Unit> {
        return userService.updateStudentVerificationStatus(uid, verified)
    }

    fun listenStudentVerification(uid: String, onChange: (StudentVerification?) -> Unit) {
        verificationService.listenStudentVerification(uid, onChange)
    }

    fun listenUserCore(uid: String, onChange: (UserCore?) -> Unit) {
        userService.listenUserCore(uid, onChange)
    }
    // Trong ProfileRepository.kt
    fun listenEmployerProfile(uid: String, onChange: (EmployerProfile?) -> Unit) {
        employerService.listenEmployerProfile(uid, onChange)
    }

    fun listenEmployerVerification(uid: String, onChange: (EmployerVerification?) -> Unit) {
        verificationService.listenEmployerVerification(uid, onChange)
    }
    fun listenStudentProfile(
        uid: String,
        onChange: (StudentProfile?) -> Unit
    ) {
        studentService.listenStudentProfile(
            uid,
            onChange
        )
    }
}