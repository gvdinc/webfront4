package remote.dto

import data.Variant
import kotlinx.serialization.Serializable

@Serializable
data class ShotsRequest (
    val login: String,
    val password_hash: String,
    val area_id: String
)