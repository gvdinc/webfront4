package data

import data.AuthState

data class Credentials(
    var login: String = "s",
    var password: String = "s",
    var state: AuthState = AuthState.UNAUTHORIZED
) {
    override fun toString(): String {
        return "data.Credentials(login=$login, password=$password, state=$state)"
    }
}

