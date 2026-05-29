package com.studentjobs.app.feature.schedule.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.student.BusyTimeSlot

@Composable
fun SubjectCard(

    item: BusyTimeSlot

) {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .background(

                color =
                    Color(0xFFF5F7FA),

                shape =
                    RoundedCornerShape(16.dp)
            )
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {

        Text(

            text =
                getDayText(item.dayOfWeek),

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )

        Text(

            text =
                "${minuteToTime(item.startMinute)} - ${
                    minuteToTime(item.endMinute)
                }",

            color =
                Color(0xFF2962FF)
        )

        Text(

            text =
                "Busy Schedule",

            color =
                Color.DarkGray
        )
    }
}

@SuppressLint("DefaultLocale")
private fun minuteToTime(

    minute: Int

): String {

    val hour =
        minute / 60

    val min =
        minute % 60

    return String.format(
        "%02d:%02d",
        hour,
        min
    )
}

private fun getDayText(

    day: Int

): String {

    return when (day) {

        1 -> "Monday"

        2 -> "Tuesday"

        3 -> "Wednesday"

        4 -> "Thursday"

        5 -> "Friday"

        6 -> "Saturday"

        7 -> "Sunday"

        else -> "Unknown Day"
    }
}