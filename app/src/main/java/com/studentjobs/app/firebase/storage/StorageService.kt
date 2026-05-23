package com.studentjobs.app.firebase.storage

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageService {

    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadStudentCard(
        uri: Uri, type: String // "front" | "back"
    ): Result<String> {
        return try {

            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            val fileName =
                if (type == "front")
                    "front.jpg"
                else
                    "back.jpg"

            val ref = storage.reference.child("student_cards/$uid/$fileName")

            // metadata (optional nhưng nên có)
            val metadata =
                StorageMetadata.Builder().setContentType("image/jpeg").setCustomMetadata("uid", uid)
                    .setCustomMetadata("type", type).build()

            // upload
            ref.putFile(uri, metadata).await()

            val downloadUrl = ref.downloadUrl.await().toString()

            Result.success(downloadUrl)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun uploadEmployerImage(
        uid: String, uri: Uri, folder: String
    ): String {

        val storageRef =
            FirebaseStorage.getInstance().reference.child("employer_verification/$uid/$folder")

        storageRef.putFile(uri).await()

        return storageRef.downloadUrl.await().toString()
    }
}