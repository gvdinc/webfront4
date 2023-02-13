import components.*
import data.AuthState
import data.Credentials
import data.HeaderData
import react.*
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

        myHeader()
        p{+ state.authState.toString()}
        if (state.authState == AuthState.UNAUTHORIZED){
            authForm(::resetStatus)
        }
        else{

        }
    }

    private fun resetStatus(credentials: Credentials){
        if (credentials.state == AuthState.AUTHORIZED) {
            console.log("user successfully authorised ${credentials.login}, " +
                    credentials.password//  .replace(regex = Regex("."), replacement = "*")
            )
            props.credentials.login = credentials.login
            props.credentials.password = credentials.password
            props.credentials.state = credentials.state
        }
        else{
            console.log("authorisation failed")
        }
        setState(AuthorizationState(authState = props.credentials.state))
    }

}

