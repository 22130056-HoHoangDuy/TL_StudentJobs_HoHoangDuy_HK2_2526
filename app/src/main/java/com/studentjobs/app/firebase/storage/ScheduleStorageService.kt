package com.studentjobs.app.firebase.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ScheduleStorageService {

    private val storage =
        FirebaseStorage.getInstance()

    suspend fun uploadTimetableImage(

        uid: String,
        imageUri: Uri

    ): String {

        val ref = storage
            .reference
            .child(
                "student_schedules/$uid/timetable.jpg"
            )

        ref.putFile(imageUri).await()

        return ref
            .downloadUrl
            .await()
            .toString()
    }
}