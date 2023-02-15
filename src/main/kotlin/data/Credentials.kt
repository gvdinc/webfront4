package data

data class Credentials(
    var login: String = "",
    var password: String = "",
    var state: AuthState = AuthState.UNAUTHORIZED
) {
    override fun toString(): String {
        return "data.Credentials(login=$login, password=$password, state=$state)"
    }
}

