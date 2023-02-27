import components.authForm
import components.myHeader
import data.AuthState
import data.Credentials
import data.Variant
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import remote.dto.ShotRequest

external interface AppProps : RProps{
    var credentials: Credentials
    var variant: Variant
}

data class AppState(
    val authState: AuthState = AuthState.UNAUTHORIZED, val variant: Variant = Variant.VADIM) : RState


class App(props: AppProps) : RComponent<AppProps, AppState>(props){
    private val client = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    init {
        state = AppState(props.credentials.state)
    }

    override fun RBuilder.render() {
        myHeader(state.variant, ::changeVariant) // here - logic of variant choice
        div("content_wrapper") {
            if (state.authState == AuthState.UNAUTHORIZED) {
                authForm(::authFormUpdate, client)
            } else {
                when (state.variant){
                    Variant.VADIM -> {
                        child(Panel::class) {
                            attrs.exitFunction = (::authExit)
                            attrs.credentials = props.credentials
                            attrs.httpClient = client
                            attrs.coordinates = ShotRequest(props.credentials.login, props.credentials.password, area_id = Variant.VADIM.area_id)
                        }
                    }
                    Variant.MIRON -> {
                        child(PanelM::class) {
                            attrs.exitFunction = (::authExit)
                            attrs.credentials = props.credentials
                            attrs.httpClient = client
                            attrs.coordinates = ShotRequest(props.credentials.login, props.credentials.password, area_id = Variant.MIRON.area_id)
                        }
                    }
                }

            }
        }
    }
    private fun changeVariant(){
        if (state.variant == Variant.VADIM) {setState(AppState(state.authState, Variant.MIRON))}
        else {setState(AppState(state.authState, Variant.VADIM))}
    }

    private fun authExit(){
        credentialsUpdate(null)
        stateUpdate()
        console.log("user logged out")
    }

    private fun authFormUpdate(credentials: Credentials){
        if (credentials.state == AuthState.AUTHORIZED) {
            console.log("user successfully authorised ${credentials.login}, " +
                    credentials.password.replace(regex = Regex("."), replacement = "*")
            )
            credentialsUpdate(credentials)
        }
        else{
            console.log("authorisation failed")
        }
        stateUpdate()
    }

    private fun stateUpdate(){
        setState(AppState(authState = props.credentials.state))
    }

    private fun credentialsUpdate(newCred: Credentials?){
        if (newCred == null){
            props.credentials.login = ""
            props.credentials.password = ""
            props.credentials.state = AuthState.UNAUTHORIZED
            stateUpdate()
        }
        else{
            props.credentials.login = newCred.login
            props.credentials.password = newCred.password
            props.credentials.state = newCred.state
        }
    }
}

