import components.InputsBlock
import data.PanelStateOptions
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import remote.dto.ShotRequest


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
        div ("panel_wrapper"){
            button(classes = "user_escape") {
                +"exit"
                attrs {
                    onClickFunction = {
                        props.exitFunction()
                    }
                }
            }

            div("dashboard"){
                div("display"){
                    //svg()
                }
                child(InputsBlock::class){
                    attrs.coordinates = ShotRequest()
                    attrs.requestFunction = (::shotRequestPOST)
                }
            }
            div("datatable"){
                table {

                }
            }
        }
    }

    private fun logoutPOST(){

    }

    private fun shotRequestPOST(shotRequest: ShotRequest){
        console.log("sending request: $shotRequest")
    }
}