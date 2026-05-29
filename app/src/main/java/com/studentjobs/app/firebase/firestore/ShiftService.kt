package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.job.ShiftEntity
import kotlinx.coroutines.tasks.await

class ShiftService {

    private val db =
        FirebaseFirestore.getInstance()

    companion object {

        private const val COLLECTION =
            "shifts"
    }

    // ========================================
    // CREATE SHIFT
    // ========================================

    suspend fun createShift(
        shift: ShiftEntity
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)
                .document(shift.shiftId)
                .set(shift)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // CREATE MANY SHIFTS
    // ========================================

    suspend fun createShifts(
        shifts: List<ShiftEntity>
    ): Result<Unit> {

        return try {

            val batch =
                db.batch()

            shifts.forEach { shift ->

                val docRef =
                    db.collection(COLLECTION)
                        .document(shift.shiftId)

                batch.set(
                    docRef,
                    shift
                )
            }

            batch.commit()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // GET SHIFT
    // ========================================

    suspend fun getShift(
        shiftId: String
    ): ShiftEntity? {

        return try {

            db.collection(COLLECTION)
                .document(shiftId)
                .get()
                .await()
                .toObject(
                    ShiftEntity::class.java
                )

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // ========================================
    // GET SHIFTS BY JOB
    // ========================================

    suspend fun getShiftsByJob(
        jobId: String
    ): List<ShiftEntity> {

        return try {

            db.collection(COLLECTION)

                .whereEqualTo(
                    "jobId",
                    jobId
                )

                .get()
                .await()

                .toObjects(
                    ShiftEntity::class.java
                )

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    // ========================================
    // UPDATE SHIFT
    // ========================================

    suspend fun updateShift(
        shift: ShiftEntity
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)
                .document(shift.shiftId)
                .set(shift)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // DELETE SHIFT
    // ========================================

    suspend fun deleteShift(
        shiftId: String
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)
                .document(shiftId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // DELETE SHIFTS BY JOB
    // ========================================

    suspend fun deleteShiftsByJob(
        jobId: String
    ): Result<Unit> {

        return try {

            val snapshot =

                db.collection(COLLECTION)

                    .whereEqualTo(
                        "jobId",
                        jobId
                    )

                    .get()
                    .await()

            val batch =
                db.batch()

            snapshot.documents.forEach {

                batch.delete(
                    it.reference
                )
            }

            batch.commit()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }
}