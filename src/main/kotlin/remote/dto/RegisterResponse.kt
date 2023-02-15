package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse (
    val register_state: String,
) {
    override fun toString(): String {
        return "RegisterResponse(register_state='$register_state')"
    }
}