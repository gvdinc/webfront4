package components

import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import remote.dto.RegisterRequest
import remote.dto.ShotRequest

external interface InputsBlockProps: RProps {
    var requestFunction: (ShotRequest) -> Unit
    var coordinates: ShotRequest
}

data class InputState(val coords: ShotRequest) : RState

class InputsBlock(props: InputsBlockProps): RComponent<InputsBlockProps, InputState>(props) {
    init {
        state = InputState(props.coordinates)
    }

    override fun RBuilder.render() {
        div("input_block"){
            p{+ state.coords.findMistakes().toString(); style("color: red;")}
            input (classes = "inputs_x"){
                attrs{onChangeFunction = { event -> props.coordinates.x =
                    try{(event.target as HTMLInputElement).value.toDouble()}
                    catch (e: NumberFormatException){
                        console.error("unable to cast to Double: ${e.message}")
                        null}
                    setState(InputState(props.coordinates))
                    }
                } }
            input (classes = "inputs_y"){
                attrs{onChangeFunction = { event ->  props.coordinates.y =
                    try{(event.target as HTMLInputElement).value.toDouble()}
                    catch (e: NumberFormatException){
                        console.error("unable to cast to Double: ${e.message}")
                        null}
                    setState(InputState(props.coordinates))
                    }
                } }
            input (classes = "inputs_R"){
                attrs{onChangeFunction = { event -> props.coordinates.R =
                    try{(event.target as HTMLInputElement).value.toDouble()}
                    catch (e: NumberFormatException){
                        console.error("unable to cast to Double: ${e.message}")
                        null}
                    setState(InputState(props.coordinates))
                    }
                } }
        }
        button (classes = "shot_button"){ + "Отправить"
            attrs.onClickFunction = {
                if (state.coords.valid()) props.requestFunction(state.coords)
            }
        }
        p{
           + "x:${state.coords.x} y:${state.coords.y} R:${state.coords.R}"
        }
    }
}