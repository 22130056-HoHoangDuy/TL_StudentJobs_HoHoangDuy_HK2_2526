package com.studentjobs.app.data.repository.student

import com.studentjobs.app.data.model.student.StudentProfile
import com.studentjobs.app.firebase.firestore.StudentService

class StudentRepository(

    private val studentService: StudentService

) {

    suspend fun createStudentProfile(
        profile: StudentProfile
    ): Result<Unit> {

        return studentService
            .createStudentProfile(
                profile
            )
    }

    suspend fun getStudentProfile(
        uid: String
    ): StudentProfile? {

        return studentService
            .getStudentProfile(
                uid
            )
    }

    suspend fun updateStudentProfile(
        profile: StudentProfile
    ): Result<Unit> {

        return studentService
            .updateStudentProfile(
                profile
            )
    }
}