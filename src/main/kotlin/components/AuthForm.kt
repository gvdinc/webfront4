package components


import data.AuthState
import data.Credentials
import io.ktor.client.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import remote.AuthService
import remote.dto.LoginRequest
import remote.dto.LoginResponse
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse

external interface AuthFormProps: RProps{
    var login: (Credentials) -> Unit
    var httpClient: HttpClient
}


val AuthForm: FunctionalComponent<AuthFormProps> = functionalComponent { props ->
    var loginVar: String = ""
    var passVar: String = ""
    val authService = AuthService.create(props.httpClient)
    var regResponse: RegisterResponse = RegisterResponse(register_state = "not requested")
    var loginResponse: LoginResponse = LoginResponse(login_state = "not requested")

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
                id = "login_button"
                onClickFunction = {
                    val request = LoginRequest(login = loginVar, password_hash = passVar)

                    var job = CoroutineScope(Dispatchers.Main).launch {
                        val resp: LoginResponse =
                            try {
                                val res: LoginResponse =
                                    authService.loginRequest(request)
                                //if (res == null) res = LoginResponse(login_state = "failed")
                                res
                            } catch (e: IOException) {
                                LoginResponse(login_state = "failed")
                            }
                        console.log("Request finished: response - $resp")
                        loginResponse = resp
                        if (loginResponse.login_state == "logon"){
                            console.log("login success: $regResponse")
                            props.login(Credentials(loginVar, passVar, AuthState.AUTHORIZED))
                        } else if (loginResponse.login_state == "wrong_login"){
                            console.error("login incorrect: $regResponse" )
                        } else if (loginResponse.login_state == "wrong_login"){
                            console.error("password incorrect: $regResponse")
                        } else {
                            console.error("unknown error during login request")}
                    }
                }
            }
        }
        button {
            + "Register"
            attrs{
                id = "register_button"
                onClickFunction = {
                    val request = RegisterRequest(login = loginVar, password_hash = passVar)

                    var job = CoroutineScope(Dispatchers.Main).launch() {
                        val resp: RegisterResponse =
                            try {
                                val res: RegisterResponse =
                                    authService.registerRequest(request)
                                //if (res == null) res = RegisterResponse(register_state = "failed")
                                res
                            } catch (e: IOException) {
                                RegisterResponse(register_state = "failed")
                            }
                        console.log("Request finished: response - $resp")
                        regResponse = resp
                        if (regResponse.register_state == "registered"){
                            console.log("registration success: $regResponse")
                            props.login(Credentials(loginVar, passVar, AuthState.AUTHORIZED))
                        } else{
                            console.error("registration failed: $regResponse" )
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.authForm(loginFunc: (Credentials) -> Unit, client: HttpClient) = child(AuthForm) {
    attrs.login = loginFunc
    attrs.httpClient = client
}







