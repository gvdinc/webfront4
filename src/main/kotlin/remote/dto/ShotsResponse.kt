package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShotsResponse(
    var shots: List<ShotsResponseElement>?
)

@Serializable
data class ShotsResponseElement(
    val datetime: String, // в формате ISO 8601
    val x: Double,
    val y: Double,
    val R: Double,
    val hit: Boolean, // true или false
    val processing_time_nano: Int, // в наносекундах
)
