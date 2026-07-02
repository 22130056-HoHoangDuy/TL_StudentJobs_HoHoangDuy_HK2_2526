package com.studentjobs.app.feature.recommendation

import com.studentjobs.app.data.model.job.ShiftEntity
import com.studentjobs.app.data.model.student.BusyTimeSlot

object TravelConstraintUtils {

    fun getTravelBufferMinutes(
        distanceKm: Double
    ): Int {

        return when {

            distanceKm <= 5.0 ->
                15

            distanceKm <= 10.0 ->
                30

            distanceKm <= 20.0 ->
                45

            else ->
                Int.MAX_VALUE
        }
    }

    fun hasEnoughTravelTime(

        busySlots: List<BusyTimeSlot>,

        shifts: List<ShiftEntity>,

        distanceKm: Double

    ): Boolean {

        val buffer =

            getTravelBufferMinutes(
                distanceKm
            )

        for (busy in busySlots) {

            for (shift in shifts) {

                if (
                    busy.dayOfWeek
                    !=
                    shift.dayOfWeek
                ) {
                    continue
                }

                val mustLeaveAt =

                    shift.startMinute -
                            buffer

                if (
                    mustLeaveAt <
                    busy.endMinute
                ) {

                    return false
                }
            }
        }

        return true
    }
}