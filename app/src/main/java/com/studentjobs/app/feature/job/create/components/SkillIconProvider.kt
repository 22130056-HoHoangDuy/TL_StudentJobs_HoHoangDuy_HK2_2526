package com.studentjobs.app.feature.job.create.components

fun getSkillLabel(
    skill: String
): String {

    return when (skill) {

        "Pha chế" ->
            "☕ Pha chế"

        "Phục vụ" ->
            "🍽️ Phục vụ"

        "Giao tiếp khách hàng" ->
            "💬 Giao tiếp"

        "Thu ngân" ->
            "💵 Thu ngân"

        "Dọn dẹp" ->
            "🧹 Dọn dẹp"

        "Sắp xếp hàng hóa" ->
            "📦 Sắp xếp hàng hóa"

        "Bán hàng" ->
            "🛍️ Bán hàng"

        "Tư vấn khách hàng" ->
            "🎧 Tư vấn khách hàng"

        else ->
            "✨ $skill"
    }
}