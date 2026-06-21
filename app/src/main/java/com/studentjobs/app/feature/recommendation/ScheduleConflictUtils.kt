package com.studentjobs.app.feature.recommendation

import com.studentjobs.app.data.model.job.ShiftEntity
import com.studentjobs.app.data.model.student.BusyTimeSlot

object ScheduleConflictUtils {

    fun hasConflict(

        busySlots: List<BusyTimeSlot>,

        shifts: List<ShiftEntity>

    ): Boolean {

        for (busy in busySlots) {

            for (shift in shifts) {

                if (
                    busy.dayOfWeek ==
                    shift.dayOfWeek
                ) {

                    val overlap =

                        busy.startMinute <
                                shift.endMinute &&

                                busy.endMinute >
                                shift.startMinute

                    if (overlap) {

                        return true
                    }
                }
            }
        }

        return false
    }
}