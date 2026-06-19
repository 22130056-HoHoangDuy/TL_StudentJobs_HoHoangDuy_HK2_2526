package com.studentjobs.app.data.repository.schedule

import android.net.Uri
import com.studentjobs.app.data.model.student.StudentSchedule
import com.studentjobs.app.firebase.firestore.StudentScheduleService
import com.studentjobs.app.firebase.storage.ScheduleStorageService
import java.util.Date

class ScheduleRepository {

    private val firestoreService =
        StudentScheduleService()

    private val storageService =
        ScheduleStorageService()

    // ========================================
    // UPLOAD TIMETABLE
    // ========================================

    suspend fun uploadTimetable(

        uid: String,

        imageUri: Uri

    ): Result<String> {

        return try {

            // ========================================
            // UPLOAD IMAGE
            // ========================================

            val imageUrl =

                storageService
                    .uploadTimetableImage(

                        uid = uid,

                        imageUri = imageUri
                    )

            // ========================================
            // CREATE FIRESTORE DOCUMENT
            // ========================================

            val schedule =

                StudentSchedule(

                    uid = uid,

                    timetableImageUrl =
                        imageUrl,

                    ocrProcessed = false,

                    createdAt = Date(),

                    updatedAt = Date()
                )

            firestoreService
                .saveSchedule(schedule)

            // ========================================
            // SUCCESS
            // ========================================

            Result.success(imageUrl)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // ========================================
    // GET SCHEDULE
    // ========================================

    suspend fun getSchedule(

        uid: String

    ): StudentSchedule? {

        return firestoreService
            .getSchedule(uid)
    }
}