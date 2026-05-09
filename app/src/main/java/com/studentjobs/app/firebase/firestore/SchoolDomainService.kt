package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SchoolDomainService {

    private val db = FirebaseFirestore.getInstance()

    suspend fun isValidStudentDomain(
        domain: String
    ): Boolean {

        return try {

            val result = db.collection("school_domains")
                .whereEqualTo("domain", domain)
                .whereEqualTo("isActive", true)
                .get()
                .await()

            !result.isEmpty

        } catch (e: Exception) {
            false
        }
    }
}