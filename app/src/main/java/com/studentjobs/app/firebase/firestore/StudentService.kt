package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.student.StudentProfile
import kotlinx.coroutines.tasks.await

class StudentService {

    private val db = FirebaseFirestore.getInstance()

    // =========================
    // CREATE STUDENT PROFILE
    // =========================
    suspend fun createStudentProfile(
        student: StudentProfile
    ): Result<Unit> {

        return try {

            db.collection("students")
                .document(student.uid)
                .set(student)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // =========================
    // GET STUDENT PROFILE
    // =========================
    suspend fun getStudentProfile(
        uid: String
    ): StudentProfile? {

        return try {

            db.collection("students")
                .document(uid)
                .get()
                .await()
                .toObject(StudentProfile::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // =========================
    // UPDATE STUDENT PROFILE
    // =========================
    suspend fun updateStudentProfile(
        student: StudentProfile
    ): Result<Unit> {

        return try {

            db.collection("students")
                .document(student.uid)
                .set(student)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }
}