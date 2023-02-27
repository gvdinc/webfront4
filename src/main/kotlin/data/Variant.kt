package data

enum class Variant (
    val area_id: String = "Vadim",
    val classPrefix: String,
    val fullName: String,
    val variantNumber: String,
    val group: String = "P32101",
){
    VADIM("Vadim","V", "Grebenkin Vadim", "331"),
    MIRON("Miron","M", "Zenin Miron", "281")
}