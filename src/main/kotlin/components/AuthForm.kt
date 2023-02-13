package components

import data.Credentials
import data.AuthState
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.RBuilder
import react.RProps
import react.dom.*

external interface AuthFormProps: RProps{
    var login: (Credentials) -> Unit
}

val AuthForm: FunctionalComponent<AuthFormProps> = functionalComponent { props ->
    var loginVar: String = ""
    var passVar: String = ""

    div("auth_form"){
        div("log_div"){
            label("login_label"){+ "Login"}
            input(classes = "input", type = InputType.text){
                attrs{
                    id = "login_input"
                    onChangeFunction = { event ->
                        loginVar = (event.target as HTMLInputElement).value
                    }
                }
            }
        }
        div("pass_div"){
            label("pass_label"){+ "Password"}
            input(classes = "input", type = InputType.password){
                attrs{
                    id = "password_input"
                    onChangeFunction = { event ->
                        passVar = (event.target as HTMLInputElement).value
                    }
                }
            }
        }
        button {
            + "Login"
            attrs{
                onClickFunction = { event ->
                    props.login(
                        Credentials(
                        login = loginVar,
                        password = passVar,
                        state = AuthState.AUTHORIZED)
                )
                    // TODO : переделать логику входа
                }
            }
        }
    }
}

fun RBuilder.authForm(loginFunc: (Credentials) -> Unit) = child(AuthForm) {
    attrs.login = loginFunc
}






