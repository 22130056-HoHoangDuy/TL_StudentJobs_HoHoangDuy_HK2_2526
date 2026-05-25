const admin = require("firebase-admin");

const vision = require("@google-cloud/vision");

const {

    onDocumentCreated

} = require(

    "firebase-functions/v2/firestore"
);

const {

    parseScheduleText

} = require("./scheduleParser");

const client =
    new vision.ImageAnnotatorClient();

exports.processStudentSchedule =

    onDocumentCreated(

        "student_schedules/{uid}",

        async (event) => {

            try {

                const data =
                    event.data.data();

                const imageUrl =
                    data.timetableImageUrl;

                if (!imageUrl) {

                    return null;
                }

                // ====================================
                // OCR
                // ====================================

                const [result] =

                    await client.textDetection({

                        image: {

                            source: {

                                imageUri:
                                    imageUrl
                            }
                        }
                    });

                const text =

                    result.fullTextAnnotation
                        ?.text || "";

                console.log(
                    "OCR TEXT:",
                    text
                );

                // ====================================
                // PARSE
                // ====================================

                const parsed =
                    parseScheduleText(text);

                // ====================================
                // UPDATE FIRESTORE
                // ====================================

                await event.data.ref.update({

                    busySlots:
                        parsed.busySlots,

                    daysWithSchedule:
                        parsed.daysWithSchedule,

                    ocrProcessed: true,

                    ocrConfidence:
                        parsed.ocrConfidence,

                    updatedAt: Date.now()
                });

                return null;

            } catch (e) {

                console.error(e);

                return null;
            }
        }
    );