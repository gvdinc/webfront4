package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShotsRequest (
    val login: String,
    val password_hash: String,
)