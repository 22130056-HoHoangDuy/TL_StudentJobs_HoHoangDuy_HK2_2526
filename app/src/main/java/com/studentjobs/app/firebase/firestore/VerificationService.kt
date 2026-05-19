package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.employer.EmployerVerification
import com.studentjobs.app.data.model.student.StudentVerification
import kotlinx.coroutines.tasks.await

class VerificationService {

    private val db = FirebaseFirestore.getInstance()

    // ========================================
    // CREATE STUDENT VERIFICATION
    // ========================================
    suspend fun createStudentVerification(
        verification: StudentVerification
    ): Result<Unit> {

        return try {

            db.collection("student_verifications")
                .document(verification.uid)
                .set(verification)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // GET STUDENT VERIFICATION
    // ========================================
    suspend fun getStudentVerification(
        uid: String
    ): StudentVerification? {

        return try {

            db.collection("student_verifications")
                .document(uid)
                .get()
                .await()
                .toObject(StudentVerification::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // ========================================
    // CREATE EMPLOYER VERIFICATION
    // ========================================
    suspend fun createEmployerVerification(
        verification: EmployerVerification
    ): Result<Unit> {

        return try {

            db.collection("employer_verifications")
                .document(verification.uid)
                .set(verification)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // GET EMPLOYER VERIFICATION
    // ========================================
    suspend fun getEmployerVerification(
        uid: String
    ): EmployerVerification? {

        return try {

            db.collection("employer_verifications")
                .document(uid)
                .get()
                .await()
                .toObject(EmployerVerification::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }
}