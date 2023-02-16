package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val register_state: String?,
    val wrong_length_params: List<String>? = null,
    val wrong_login_character: String? = null,
    /*
    "register_state", значения "registered", "duplicate_login" или "bad_content" (следующие два)
    "wrong_length_params", массив с ["login", "password"] - смотря что имеет неправильную длину
    "wrong_login_character" с
     */
) {
    override fun toString(): String {
        var line =  "RegisterResponse( register_state='$register_state'"
        if (wrong_length_params != null) line += "\n wrong_length_params= $wrong_length_params"
        if (wrong_login_character != null) line += "\n wrong_length_params= $wrong_login_character"
        line += " )"
        return line
    }
}