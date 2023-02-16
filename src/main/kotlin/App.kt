import components.authForm
import components.myHeader
import data.AuthState
import data.Credentials
import data.HeaderData
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.p


external interface AppProps : RProps{
    var credentials: Credentials
    val headerData: HeaderData
}

data class AuthorizationState(val authState: AuthState = AuthState.UNAUTHORIZED) : RState


class App(props: AppProps) : RComponent<AppProps, AuthorizationState>(props){


    init {
        state = AuthorizationState(props.credentials.state)
    }

    override fun RBuilder.render() {

        val header = myHeader()

        p{+ state.authState.toString()}
        if (state.authState == AuthState.UNAUTHORIZED){
            authForm(::authFormUpdate)
        }
        else{
            child(Panel::class){
                attrs.exitFunction = (::authExit)
            }
        }
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
        setState(AuthorizationState(authState = props.credentials.state))
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

