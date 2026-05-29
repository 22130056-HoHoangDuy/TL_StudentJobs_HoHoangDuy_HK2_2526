package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.student.StudentSchedule
import kotlinx.coroutines.tasks.await

class StudentScheduleService {

    private val firestore =
        FirebaseFirestore.getInstance()

    companion object {

        private const val COLLECTION =
            "student_schedules"
    }

    suspend fun saveSchedule(

        schedule: StudentSchedule

    ) {

        firestore
            .collection(COLLECTION)
            .document(schedule.uid)
            .set(schedule)
            .await()
    }

    suspend fun getSchedule(

        uid: String

    ): StudentSchedule? {

        return firestore
            .collection(COLLECTION)
            .document(uid)
            .get()
            .await()
            .toObject(StudentSchedule::class.java)
    }
}