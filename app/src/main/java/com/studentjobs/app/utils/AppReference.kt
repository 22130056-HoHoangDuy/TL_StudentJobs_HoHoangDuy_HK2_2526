package com.studentjobs.app.utils

import android.content.Context

class AppPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun setOnboardingShown() {
        prefs.edit().putBoolean("onboarding_shown", true).apply()
    }

    fun isOnboardingShown(): Boolean {
        return prefs.getBoolean("onboarding_shown", false)
    }

    fun saveUserRole(role: String) {
        prefs.edit().putString("user_role", role).apply()
    }

    fun getUserRole(): String {
        return prefs.getString("user_role", "STUDENT") ?: "STUDENT"
    }
}