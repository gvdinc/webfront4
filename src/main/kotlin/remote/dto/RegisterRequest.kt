package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password_hash: String,
)
