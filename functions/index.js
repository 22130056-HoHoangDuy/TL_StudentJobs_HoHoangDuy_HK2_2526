require("dotenv").config();
const nodemailer =
  require("nodemailer");
const { onObjectFinalized } =
  require("firebase-functions/v2/storage");

const {
  onCall,
  HttpsError
} = require(
  "firebase-functions/v2/https"
);

const admin = require("firebase-admin");

const vision = require("@google-cloud/vision");
const client = new vision.ImageAnnotatorClient();

const path = require("path");

const os = require("os");
const transporter =
  nodemailer.createTransport({

    service: "gmail",

    auth: {
      user: process.env.GMAIL_EMAIL,
      pass: process.env.GMAIL_PASSWORD,
    },
});

admin.initializeApp();

exports.processStudentCardV2 = onObjectFinalized(
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
exports.sendVerificationOtp = onCall(
  {
    region: "us-east1",
  },
  async (request) => {

    try {

      const data = request.data;

      const email = data.email;
      const uid = data.uid;

      if (!email || !uid) {
        throw new Error("Missing email or uid");
      }

      // GENERATE OTP
      const otp =
        Math.floor(
          100000 + Math.random() * 900000
        ).toString();

      // SAVE OTP
      await admin.firestore()
        .collection("email_otps")
        .doc(uid)
        .set({
          email: email,
          otp: otp,
          createdAt: Date.now(),
        });

      // SEND EMAIL
      await transporter.sendMail({
        from: process.env.GMAIL_EMAIL,
        to: email,
        subject: "StudentJobs Verification OTP",
        text: `Your OTP code is: ${otp}`,
      });

      return {
        success: true,
      };

    } catch (err) {

        console.error("OTP ERROR:", err);

        throw new HttpsError(
          "internal",
          err.message || "OTP send failed"
        );
      }
  }
);