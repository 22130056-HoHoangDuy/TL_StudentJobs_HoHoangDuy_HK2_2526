const admin =
  require("firebase-admin");

admin.initializeApp();

// ======================================
// VERIFICATION
// ======================================

const {
  processStudentCardV2
} = require(
  "./verification/processStudentCard"
);

// ======================================
// OTP
// ======================================

const {
  sendVerificationOtp
} = require(
  "./otp/sendVerificationOtp"
);

// Timetable

const {
  processStudentSchedulePy
} = require(
  "./schedule/processStudentSchedulePy"
);


module.exports = {

  processStudentCardV2,

  sendVerificationOtp,

  processStudentSchedulePy

};
