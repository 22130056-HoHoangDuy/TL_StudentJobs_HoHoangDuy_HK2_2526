package com.studentjobs.app.utils

fun dayOfWeekText(
    day: Int
): String {

    return when (day) {

        1 -> "Thứ 2"

        2 -> "Thứ 3"

        3 -> "Thứ 4"

        4 -> "Thứ 5"

        5 -> "Thứ 6"

        6 -> "Thứ 7"

        7 -> "Chủ nhật"

        else -> "Không xác định"
    }
}

fun minuteToTime(
    minute: Int
): String {

    val hour = minute / 60

    val min = minute % 60

    return "%02d:%02d".format(
        hour,
        min
    )
}