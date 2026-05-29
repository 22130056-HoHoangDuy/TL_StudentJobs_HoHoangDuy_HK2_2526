package com.studentjobs.app.data.model.skill

object SkillCatalog {

    val cafeSkills = listOf(

        SkillEntity("SK001", "Pha chế"),
        SkillEntity("SK002", "Phục vụ"),
        SkillEntity("SK003", "Thu ngân"),
        SkillEntity("SK004", "Order món"),
        SkillEntity("SK005", "Giao tiếp khách hàng"),
        SkillEntity("SK006", "Dọn dẹp khu vực")
    )

    val restaurantSkills = listOf(

        SkillEntity("SK007", "Phục vụ bàn"),
        SkillEntity("SK008", "Bưng bê"),
        SkillEntity("SK009", "Thu ngân"),
        SkillEntity("SK010", "Order món"),
        SkillEntity("SK011", "Làm việc nhóm"),
        SkillEntity("SK012", "Giao tiếp khách hàng")
    )

    val convenienceStoreSkills = listOf(

        SkillEntity("SK013", "Thu ngân"),
        SkillEntity("SK014", "Sắp xếp hàng hóa"),
        SkillEntity("SK015", "Kiểm kê hàng hóa"),
        SkillEntity("SK016", "Tư vấn khách hàng")
    )

    val retailSkills = listOf(

        SkillEntity("SK017", "Bán hàng"),
        SkillEntity("SK018", "Tư vấn khách hàng"),
        SkillEntity("SK019", "Thu ngân"),
        SkillEntity("SK020", "Livestream bán hàng")
    )

    val warehouseSkills = listOf(

        SkillEntity("SK021", "Đóng gói hàng hóa"),
        SkillEntity("SK022", "Phân loại hàng hóa"),
        SkillEntity("SK023", "Kiểm kê hàng hóa")
    )

    val officeSkills = listOf(

        SkillEntity("SK024", "Tin học văn phòng"),
        SkillEntity("SK025", "Excel"),
        SkillEntity("SK026", "Nhập liệu"),
        SkillEntity("SK027", "Chăm sóc khách hàng")
    )

    val eventSkills = listOf(

        SkillEntity("SK028", "PG"),
        SkillEntity("SK029", "PB"),
        SkillEntity("SK030", "Hỗ trợ sự kiện"),
        SkillEntity("SK031", "Check-in khách hàng")
    )

    fun getSkillsByCategory(
        category: String?
    ): List<SkillEntity> {

        return when (category) {

            BusinessCategory.CAFE ->
                cafeSkills

            BusinessCategory.RESTAURANT ->
                restaurantSkills

            BusinessCategory.CONVENIENCE_STORE ->
                convenienceStoreSkills

            BusinessCategory.RETAIL_SHOP ->
                retailSkills

            BusinessCategory.WAREHOUSE ->
                warehouseSkills

            BusinessCategory.OFFICE ->
                officeSkills

            BusinessCategory.EVENT ->
                eventSkills

            else ->
                emptyList()
        }
    }
}