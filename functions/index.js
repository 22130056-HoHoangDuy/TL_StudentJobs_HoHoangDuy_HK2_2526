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

// ======================================
// EXPORT
// ======================================

module.exports = {

  processStudentCardV2,

  sendVerificationOtp,
};