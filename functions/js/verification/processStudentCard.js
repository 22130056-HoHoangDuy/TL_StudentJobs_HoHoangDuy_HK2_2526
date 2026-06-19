require("dotenv").config();

const admin = require("firebase-admin");

const vision =
  require("@google-cloud/vision");

const {
  onObjectFinalized
} = require(
  "firebase-functions/v2/storage"
);

const path = require("path");

const os = require("os");

const client =
  new vision.ImageAnnotatorClient();
const processStudentCardV2 = onObjectFinalized(
  {
    region: "us-east1",
  },
  async (event) => {
    try {

      // ======================================
      // FILE INFO
      // ======================================

      const file = event.data;

      const filePath = file.name;

      const bucketName = file.bucket;

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "FUNCTION_TRIGGERED",
          filePath: filePath,
          bucketName: bucketName,
          createdAt: new Date(),
        });

      // ======================================
      // VALIDATE
      // ======================================

      if (!filePath) {

        await admin.firestore()
          .collection("debug_logs")
          .add({
            step: "NO_FILE_PATH",
            createdAt: new Date(),
          });

        return;
      }

      if (!filePath.startsWith("student_cards/")) {

        await admin.firestore()
          .collection("debug_logs")
          .add({
            step: "INVALID_FOLDER",
            filePath: filePath,
            createdAt: new Date(),
          });

        return;
      }

      // ONLY OCR FRONT IMAGE
      if (!filePath.toLowerCase().includes("front")) {

        await admin.firestore()
          .collection("debug_logs")
          .add({
            step: "SKIP_BACK_IMAGE",
            filePath: filePath,
            createdAt: new Date(),
          });

        return;
      }

      // ======================================
      // DOWNLOAD FILE
      // ======================================

      const bucket =
        admin.storage().bucket(bucketName);

      const tempFilePath = path.join(
        os.tmpdir(),
        path.basename(filePath)
      );

      await bucket.file(filePath).download({
        destination: tempFilePath,
      });

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "FILE_DOWNLOADED",
          tempFilePath: tempFilePath,
          createdAt: new Date(),
        });

      // ======================================
      // OCR
      // ======================================

      const [result] =
        await client.textDetection(
          tempFilePath
        );

      const text =
        result.fullTextAnnotation?.text || "";

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "OCR_DONE",
          textLength: text.length,
          preview: text.substring(0, 300),
          createdAt: new Date(),
        });

      const lines = text
        .split("\n")
        .map((l) => l.trim())
        .filter((l) => l.length > 0);

      // ======================================
      // PARSE DATA
      // ======================================

      let name = "";
      let studentId = "";
      let school = "";
      let dob = "";

      // SCHOOL
      for (const line of lines) {

        if (line.includes("ĐẠI HỌC")) {

          school =
            line.replace("BIDV", "").trim();

          break;
        }
      }

      // NAME
      for (const line of lines) {

        if (
          line.match(
            /^[A-ZÀ-Ỹ][a-zà-ỹ]+(\s[A-ZÀ-Ỹ][a-zà-ỹ]+)+$/
          ) &&
          !line.includes("MSSV") &&
          !line.includes("Ngày sinh") &&
          !line.includes("Ngành")
        ) {

          name = line;

          break;
        }
      }

      // MSSV
      for (const line of lines) {

        if (line.includes("MSSV")) {

          const match = line.match(/\d+/);

          if (match) {

            studentId = match[0];

            break;
          }
        }
      }

      // DOB
      for (const line of lines) {

        if (line.includes("Ngày sinh")) {

          const match =
            line.match(/\d{2}\/\d{2}\/\d{4}/);

          if (match) {

            dob = match[0];

            break;
          }
        }
      }
    // MAJOR
    let major = "";

    for (const line of lines) {

      if (line.includes("Ngành")) {

        major =
          line.replace("Ngành", "")
              .replace(":", "")
              .trim();

        break;
      }
    }
      // ======================================
      // UID
      // ======================================

      const parts = filePath.split("/");

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "PATH_SPLIT",
          parts: parts,
          createdAt: new Date(),
        });

      if (parts.length < 2) {

        await admin.firestore()
          .collection("debug_logs")
          .add({
            step: "INVALID_UID",
            filePath: filePath,
            createdAt: new Date(),
          });

        return;
      }

      const uid = parts[1];

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "UID_PARSED",
          uid: uid,
          createdAt: new Date(),
        });

      // ======================================
      // PARSED RESULT
      // ======================================

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "PARSED_RESULT",
          uid: uid,
          name: name,
          studentId: studentId,
          school: school,
          major: major,
          dob: dob,
          createdAt: new Date(),
        });
// ======================================
// TRUST SCORE
// ======================================

const userRef =
  admin.firestore()
    .collection("users")
    .doc(uid);

const userDoc =
  await userRef.get();

const currentTrust =
  userDoc.data()?.trustScore || 0;

// ======================================
// CHECK DUPLICATE
// ======================================

const trustLogSnapshot =
  await admin.firestore()
    .collection("trust_logs")
    .where(
      "userUid",
      "==",
      uid
    )
    .where(
      "actionType",
      "==",
      "STUDENT_VERIFIED"
    )
    .get();

const alreadyRewarded =
  !trustLogSnapshot.empty;

if (!alreadyRewarded) {

  await userRef.update({

    trustScore:
      Math.min(
        currentTrust + 20,
        100
      )
  });

  const trustLogRef =
    admin.firestore()
      .collection("trust_logs")
      .doc();

  await trustLogRef.set({

    trustLogId:
      trustLogRef.id,

    userUid:
      uid,

    actionType:
      "STUDENT_VERIFIED",

    changeAmount:
      20,

    severity:
      "LOW",

    description:
      "Xác thực sinh viên thành công",

    createdAt:
      new Date()
  });

  await admin.firestore()
    .collection("debug_logs")
    .add({

      step:
        "TRUST_REWARD_ADDED",

      uid,

      trustAdded:
        20,

      createdAt:
        new Date()
    });
}
      // ======================================
      // UPDATE STUDENT VERIFICATION
      // ======================================

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "BEFORE_VERIFICATION_UPDATE",
          uid: uid,
          createdAt: new Date(),
        });

      await admin
        .firestore()
        .collection("student_verifications")
        .doc(uid)
        .set(
          {
            extractedStudentName: name,
            extractedStudentId: studentId,
            extractedStudentMajor: major,
            extractedStudentSchoolName: school,
            extractedStudentDob: dob,
            studentCardVerified: "VERIFIED",
            updatedAt: new Date()

             // cleanup old legacy fields
                isCardVerified:
                    admin.firestore.FieldValue.delete(),

                  verificationStatus:
                    admin.firestore.FieldValue.delete(),

                  cardVerified:
                    admin.firestore.FieldValue.delete(),
            verificationStatus:
              "PENDING, WAIT ANOTHER VERIFICATION FLOW...",
          },
          { merge: true }
        );

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "AFTER_VERIFICATION_UPDATE",
          uid: uid,
          createdAt: new Date(),
        });

      // ======================================
      // UPDATE STUDENT PROFILE
      // ======================================

      await admin.firestore()
        .collection("students")
        .doc(uid)
        .set(
          {
            fullName: name,

            major: major,

            studentId: studentId,

            schoolName: school,

            dateOfBirth: dob,

                // cleanup old field
                  school:
                    admin.firestore.FieldValue.delete(),
          },
          { merge: true }
        );

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "AFTER_PROFILE_UPDATE",
          uid: uid,
          createdAt: new Date(),
        });

    } catch (err) {

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "ERROR",
          error: err.toString(),
          stack: err.stack || "",
          createdAt: new Date(),
        });

      console.error("ERROR:", err);
    }
  }
);
module.exports = {
  processStudentCardV2,
};