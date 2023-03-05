package remote.dto

import data.Variant
import kotlinx.serialization.Serializable

@Serializable
data class ShotsRequest (
    val login: String,
    val password: String,
    val area_id: String
)