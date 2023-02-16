import data.AuthState
import data.Credentials
import io.ktor.utils.io.errors.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


import react.dom.render
import remote.AuthService
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse


fun main() {
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
