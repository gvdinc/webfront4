package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    val login_state: String, // "logon", "wrong_login" или "wrong_password"
)