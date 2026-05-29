package com.studentjobs.app.feature.job.create

fun getDayName(
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

        else -> "Unknown"
    }
}

fun formatMinute(
    minute: Int
): String {

    val hour =
        minute / 60

    val min =
        minute % 60

    return "%02d:%02d"
        .format(
            hour,
            min
        )
}