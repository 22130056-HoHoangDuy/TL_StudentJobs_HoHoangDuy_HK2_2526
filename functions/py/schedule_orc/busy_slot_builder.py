from schedule_orc.parser_utils import time_to_minute

def build_busy_slots(

        anchors,
        subjects,
        columns
):

    busy_slots = []

    for i in range(
            len(anchors)
    ):

        anchor = anchors[i]

        subject = subjects[i]

        if subject == "N/A":
            continue

        best_day = None

        min_dist = 999999

        for col in columns:

            dist = abs(

                anchor["centerX"]
                -
                col["centerX"]
            )

            if dist < min_dist:

                min_dist = dist

                best_day = col["dayOfWeek"]

        if min_dist < 150:

            busy_slots.append({

                "dayOfWeek":
                    best_day,

                "subjectName":
                    subject,

                "startMinute":
                    time_to_minute(
                        anchor["startTime"]
                    ),

                "endMinute":
                    time_to_minute(
                        anchor["endTime"]
                    )
            })

    return busy_slots