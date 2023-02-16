package remote.dto

import kotlinx.serialization.Serializable
import kotlin.js.Date

@Serializable
data class ShotsResponse(
    val x: Double,
    val y: Double,
    val R: Double,
    val hit: Boolean, // true или false
    val datetime: String, // в формате ISO 8601
    val processing_time: Int, // в наносекундах
)
