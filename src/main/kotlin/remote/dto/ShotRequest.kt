package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShotRequest(
    val x: Double,
    val y: Double,
    val R: Double,
)
