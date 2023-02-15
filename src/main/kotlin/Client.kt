import data.AuthState
import data.Credentials
import io.ktor.utils.io.errors.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.promise



import react.dom.render
import remote.AuthService
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse


fun main() {

    val service: AuthService = AuthService.create()
    val resp : RegisterResponse = {
        try {
            var res : RegisterResponse? = service.registerRequest(RegisterRequest(login = "aaa", password_hash = "bbb"))
            if (res == null) res = RegisterResponse(register_state = "failed")
            res
        } catch (e: IOException) {RegisterResponse(register_state = "failed")}
    }

    window.onload = {
        render(document.getElementById("root")) {
            child(App::class){
                attrs{
                    credentials = Credentials(state = AuthState.AUTHORIZED)
                }
            }
        }
    }
}

