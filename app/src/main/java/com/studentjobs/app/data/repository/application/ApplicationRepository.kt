package com.studentjobs.app.data.repository.application

import com.studentjobs.app.data.model.application.ApplicationEntity
import com.studentjobs.app.firebase.firestore.ApplicationService

class ApplicationRepository(

    private val service:
    ApplicationService

) {

    suspend fun createApplication(
        application: ApplicationEntity
    ) =
        service.createApplication(
            application
        )

    suspend fun getApplicationsByStudent(
        studentUid: String
    ) =
        service.getApplicationsByStudent(
            studentUid
        )

    suspend fun getApplicationsByJob(
        jobId: String
    ) =
        service.getApplicationsByJob(
            jobId
        )

    suspend fun updateStatus(

        applicationId: String,

        status: String

    ) =
        service.updateStatus(

            applicationId,

            status
        )

    suspend fun hasApplied(

        studentUid: String,

        jobId: String

    ) =

        service.hasApplied(

            studentUid,

            jobId
        )

    suspend fun getActiveApplicationsByStudent(
        studentUid: String
    ): List<ApplicationEntity> {

        return service
            .getActiveApplicationsByStudent(
                studentUid
            )
    }

    suspend fun countActiveApplications(
        studentUid: String
    ): Int {

        return getActiveApplicationsByStudent(
            studentUid
        ).size
    }
}