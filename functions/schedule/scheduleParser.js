const {

    convertTimeToMinute

} = require("./timeUtils");

function parseScheduleText(text) {

    // ====================================
    // RESULT
    // ====================================

    const busySlots = [];

    const daysWithSchedule =
        new Set();

    // ====================================
    // CLEAN TEXT
    // ====================================

    const lines =

        text
            .split("\n")
            .map(line => line.trim())
            .filter(line => line.length > 0);

    // ====================================
    // CURRENT DAY
    // ====================================

    let currentDay = null;

    // ====================================
    // LOOP
    // ====================================

    for (

        let i = 0;

        i < lines.length;

        i++

    ) {

        const line =
            lines[i];

        // ====================================
        // DETECT DAY
        // ====================================

        const dayMatch =

            line.match(
                /Thứ\s*(\d)/i
            );

        if (dayMatch) {

            currentDay =
                Number(dayMatch[1]);

            daysWithSchedule.add(
                currentDay
            );

            continue;
        }

        // ====================================
        // DETECT TIME
        // ====================================

        const timeMatch =

            line.match(

                /(\d{2}:\d{2})\s*->\s*(\d{2}:\d{2})/
            );

        if (

            timeMatch &&

            currentDay !== null

        ) {

            // ====================================
            // SUBJECT NAME
            // ====================================

            let subjectName =
                "Unknown Subject";

            if (i > 0) {

                subjectName =
                    lines[i - 1];
            }

            // ====================================
            // CREATE SLOT
            // ====================================

            busySlots.push({

                subjectName,

                dayOfWeek:
                    currentDay,

                startMinute:

                    convertTimeToMinute(
                        timeMatch[1]
                    ),

                endMinute:

                    convertTimeToMinute(
                        timeMatch[2]
                    )
            });
        }
    }

    // ====================================
    // OCR CONFIDENCE
    // ====================================

    let ocrConfidence = 0.0;

    if (busySlots.length > 0) {

        ocrConfidence = 0.8;
    }

    // ====================================
    // RETURN
    // ====================================

    return {

        busySlots,

        daysWithSchedule:
            Array.from(
                daysWithSchedule
            ),

        ocrConfidence
    };
}

module.exports = {

    parseScheduleText
};