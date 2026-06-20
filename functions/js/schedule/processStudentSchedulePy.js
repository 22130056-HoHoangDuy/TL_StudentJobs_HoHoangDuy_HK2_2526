const admin = require("firebase-admin");
const axios = require("axios");

const {

    onDocumentCreated

} = require(

    "firebase-functions/v2/firestore"
);

exports.processStudentSchedulePy =

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

                console.log(
                    "CALL PYTHON OCR..."
                );

                // ============================
                // CALL PYTHON API
                // ============================

                const response =

                    await axios.post(

                        "https://wasp-kudos-snaking.ngrok-free.dev/ocr",

                        {

                            imagePath:
                                imageUrl
                        }
                    );

                const result =
                    response.data;

                console.log(
                    "PYTHON RESULT:",
                    result
                );

                // ============================
                // UPDATE FIRESTORE
                // ============================

                await event.data.ref.update({

                    busySlots:
                        result.busySlots || [],

                    daysWithSchedule:
                        result.daysWithSchedule || [],

                    ocrProcessed:
                        true,

                    updatedAt: admin.firestore.FieldValue.serverTimestamp()
                });

                return null;

            } catch (e) {

                console.error(

                    "PROCESS PY OCR ERROR:",

                    e
                );

                return null;
            }
        }
    );