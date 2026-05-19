require("dotenv").config();

const { onObjectFinalized } = require("firebase-functions/v2/storage");
const admin = require("firebase-admin");
const vision = require("@google-cloud/vision");
const { onCall } = require("firebase-functions/v2/https");
const nodemailer = require("nodemailer");

admin.initializeApp();

console.log("EMAIL:", process.env.GMAIL_EMAIL);
console.log("PASS:", process.env.GMAIL_PASSWORD);

const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: process.env.GMAIL_EMAIL,
    pass: process.env.GMAIL_PASSWORD,
  },
});

const client = new vision.ImageAnnotatorClient();

exports.processStudentCard = onObjectFinalized(
  {
    region: "us-east1",
  },
  async (event) => {
    try {
      const file = event.data;

      const filePath = file.name;
      const bucket = file.bucket;

      console.log("File uploaded:", filePath);

      // =======================
      // 🔥 VALIDATE PATH
      // =======================
      if (!filePath || !filePath.startsWith("student_cards/")) return;

      // 🔥 CHỈ OCR ẢNH FRONT
      if (!filePath.includes("front")) {
        console.log("Skip back image");
        return;
      }

      // =======================
      // 🔥 OCR
      // =======================
      const [result] = await client.textDetection(
        `gs://${bucket}/${filePath}`
      );

      const text = result.fullTextAnnotation?.text || "";

      console.log("OCR TEXT:\n", text);

      const lines = text
        .split("\n")
        .map((l) => l.trim())
        .filter((l) => l.length > 0);

      console.log("LINES:", lines);

      let name = "";
      let studentId = "";
      let school = "";
      let dob = "";

      // =======================
      // 🔥 SCHOOL
      // =======================
      for (const line of lines) {
        if (line.includes("ĐẠI HỌC")) {
          school = line.replace("BIDV", "").trim();
          break;
        }
      }

      // =======================
      // 🔥 NAME
      // =======================
      for (const line of lines) {
        if (
          line.match(/^[A-ZÀ-Ỹ][a-zà-ỹ]+(\s[A-ZÀ-Ỹ][a-zà-ỹ]+)+$/) &&
          !line.includes("MSSV") &&
          !line.includes("Ngày sinh") &&
          !line.includes("Ngành")
        ) {
          name = line;
          break;
        }
      }

      // =======================
      // 🔥 MSSV
      // =======================
      for (const line of lines) {
        if (line.includes("MSSV")) {
          const match = line.match(/\d+/);
          if (match) {
            studentId = match[0];
            break;
          }
        }
      }

      // =======================
      // 🔥 DATE OF BIRTH
      // =======================
      for (const line of lines) {
        if (line.includes("Ngày sinh")) {
          const match = line.match(/\d{2}\/\d{2}\/\d{4}/);
          if (match) {
            dob = match[0];
            break;
          }
        }
      }

      // =======================
      // 🔥 UID
      // =======================
      const parts = filePath.split("/");
      if (parts.length < 2) {
        console.log("Invalid file path");
        return;
      }

      const uid = parts[1];

      // =======================
      // 🔥 AVATAR URL (chưa dùng)
      // =======================
      const avatarUrl = `https://storage.googleapis.com/${bucket}/${filePath}`;
      console.log("Parsed:", {
        name,
        studentId,
        school,
        dob,
        uid,
        avatarUrl,
      });

      // =======================
      // 🔥 UPDATE FIRESTORE
      // =======================
      await admin.firestore().collection("users").doc(uid).update({
        extractedName: name,
        studentId: studentId,
        school: school,
        dateOfBirth: dob,
        isStudentVerified: true,
      });

      console.log("Firestore updated");
    } catch (err) {
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

      const { email, uid } = request.data;

      if (!email || !uid) {
        throw new Error("Missing email or uid");
      }

      // 🔥 Generate OTP
      const otp = Math.floor(
        100000 + Math.random() * 900000
      ).toString();

      // 🔥 Save Firestore
      await admin.firestore()
        .collection("email_otps")
        .doc(uid)
        .set({
          email,
          otp,
          createdAt: admin.firestore.FieldValue.serverTimestamp(),
        });

      // 🔥 Send Email
      await transporter.sendMail({
        from: process.env.GMAIL_EMAIL,
        to: email,
        subject: "StudentJobs Verification Code",
        text: `Your OTP is: ${otp}`,
      });

      console.log("OTP sent:", otp);

      return {
        success: true,
      };

    } catch (err) {

      console.error(err);

      throw new Error(err.message);
    }
  }
);