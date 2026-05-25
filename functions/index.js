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

// ORC Schedule
const {
  processStudentSchedule
} = require(
  "./schedule/processStudentSchedule"
);

// ======================================
// EXPORT
// ======================================

module.exports = {

  processStudentCardV2,

  sendVerificationOtp,

  processStudentSchedule,
};
