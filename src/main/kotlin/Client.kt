import data.AuthState
import data.Credentials
import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    window.onload = {
        render(document.getElementById("root")) {
            child(App::class){
                attrs{
                    credentials = Credentials(state = AuthState.UNAUTHORIZED)
                }
            }
        }
    }
}

