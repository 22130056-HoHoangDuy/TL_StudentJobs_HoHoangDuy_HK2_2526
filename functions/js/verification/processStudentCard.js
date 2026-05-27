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
          createdAt: Date.now(),
        });

      // ======================================
      // VALIDATE
      // ======================================

      if (!filePath) {

        await admin.firestore()
          .collection("debug_logs")
          .add({
            step: "NO_FILE_PATH",
            createdAt: Date.now(),
          });

        return;
      }

      if (!filePath.startsWith("student_cards/")) {

        await admin.firestore()
          .collection("debug_logs")
          .add({
            step: "INVALID_FOLDER",
            filePath: filePath,
            createdAt: Date.now(),
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
            createdAt: Date.now(),
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
          createdAt: Date.now(),
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
          createdAt: Date.now(),
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
          createdAt: Date.now(),
        });

      if (parts.length < 2) {

        await admin.firestore()
          .collection("debug_logs")
          .add({
            step: "INVALID_UID",
            filePath: filePath,
            createdAt: Date.now(),
          });

        return;
      }

      const uid = parts[1];

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "UID_PARSED",
          uid: uid,
          createdAt: Date.now(),
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
          createdAt: Date.now(),
        });

      // ======================================
      // UPDATE STUDENT VERIFICATION
      // ======================================

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "BEFORE_VERIFICATION_UPDATE",
          uid: uid,
          createdAt: Date.now(),
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
            updatedAt: Date.now(),

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
          createdAt: Date.now(),
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
          createdAt: Date.now(),
        });

    } catch (err) {

      await admin.firestore()
        .collection("debug_logs")
        .add({
          step: "ERROR",
          error: err.toString(),
          stack: err.stack || "",
          createdAt: Date.now(),
        });

      console.error("ERROR:", err);
    }
  }
);
module.exports = {
  processStudentCardV2,
};