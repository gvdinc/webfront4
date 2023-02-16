import data.PanelStateOptions
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.attrs
import react.dom.button


external interface PanelProps: RProps{
    var exitFunction : () -> Unit
    // TODO : add props
}

data class PanelState(var panelState: PanelStateOptions) : RState

class Panel(props: PanelProps): RComponent<PanelProps, PanelState>(props) {
    init {
        // TODO: add state control
    }

    override fun RBuilder.render(){

        button(classes="user_escape") { + "exit"
            attrs{
                onClickFunction = {
                    props.exitFunction()
                }
            }
        }



    }

    fun logoutPOST(){

    }
}