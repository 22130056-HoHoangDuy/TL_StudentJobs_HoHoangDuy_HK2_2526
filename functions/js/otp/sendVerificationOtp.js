require("dotenv").config();

const nodemailer =
  require("nodemailer");

const admin =
  require("firebase-admin");

const {
  onCall,
  HttpsError
} = require(
  "firebase-functions/v2/https"
);

const transporter =
  nodemailer.createTransport({

    service: "gmail",

    auth: {
      user: process.env.GMAIL_EMAIL,
      pass: process.env.GMAIL_PASSWORD,
    },
});
const sendVerificationOtp = onCall(
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
module.exports = {
  sendVerificationOtp,
};