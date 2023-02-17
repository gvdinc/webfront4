package components

import kotlinx.browser.document
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import remote.dto.RegisterRequest
import remote.dto.ShotRequest
import remote.dto.ShotsResponseElement
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.random.Random

external interface InputsBlockProps: RProps {
    var requestFunction: (ShotRequest) -> Unit
    var coordinates: ShotRequest
}

data class InputState(val coords: ShotRequest, var shotList: List<ShotsResponseElement>? = null) : RState

class InputsBlock(props: InputsBlockProps): RComponent<InputsBlockProps, InputState>(props) {
    init {
        state = InputState(props.coordinates)
    }

    override fun RBuilder.render() {
        div("display_wrapper") {

            val testArr: List<ShotsResponseElement> = listOf(
                ShotsResponseElement(1.0, 2.0, 3.0, true, "", 12),
                ShotsResponseElement(0.2, 0.0, 1.0, false, "", 122),
                ShotsResponseElement(-2.0, -4.0, 8.0, true, "", 16)
            )

            div("display") {
                val myWidth = 200.0
                mySVG(myWidth, 20.0, state.shotList)
                //attrs.onClickFunction
                attrs {
                    id = "svg_div"
                    onClickFunction = { event ->
                        console.log(event)
                        val posLeft = (event.currentTarget as HTMLDivElement).offsetLeft
                        val posTop = (event.currentTarget as HTMLDivElement).offsetTop
                        val width = (event.currentTarget as HTMLDivElement).offsetWidth
                        val posHeight = (event.currentTarget as HTMLDivElement).offsetHeight
                        console.log(
                            "first: ",
                            posLeft.toString(),
                            posTop.toString(),
                            width.toString(),
                            posHeight.toString()
                        );
                        // TODO: get cursor
                        var x = 2
                        var y = 1.5
                        if (props.coordinates.R != null){
                            props.coordinates.x = round((Random.nextDouble()*10 - 5)*10) / 10
                            props.coordinates.y = round((Random.nextDouble()*10 - 5)*10) / 10
                            setState(InputState(props.coordinates))
                        }

                    }
                }
            }


            div("input_block") {
                p { +state.coords.findMistakes().toString(); style("color: red;") }
                input(classes = "inputs_x", name = "x_input") {
                    attrs {
                        onChangeFunction = { event ->
                            props.coordinates.x =
                                try {
                                    (event.target as HTMLInputElement).value.toDouble()
                                } catch (e: NumberFormatException) {
                                    console.error("unable to cast to Double: ${e.message}")
                                    null
                                }
                            setState(InputState(props.coordinates))
                        }
                    }
                }
                input(classes = "inputs_y", name = "y_input") {
                    attrs {
                        onChangeFunction = { event ->
                            props.coordinates.y =
                                try {
                                    (event.target as HTMLInputElement).value.toDouble()
                                } catch (e: NumberFormatException) {
                                    console.error("unable to cast to Double: ${e.message}")
                                    null
                                }
                            setState(InputState(props.coordinates))
                        }
                    }
                }
                input(classes = "inputs_R") {
                    attrs {
                        onChangeFunction = { event ->
                            props.coordinates.R =
                                try {
                                    (event.target as HTMLInputElement).value.toDouble()
                                } catch (e: NumberFormatException) {
                                    console.error("unable to cast to Double: ${e.message}")
                                    null
                                }
                            setState(InputState(props.coordinates))
                        }
                    }
                }
            }
            button(classes = "shot_button") {
                +"Отправить"
                attrs.onClickFunction = {
                    if (state.coords.valid()) props.requestFunction(state.coords)
                }
            }
            p {
                +"x:${state.coords.x} y:${state.coords.y} R:${state.coords.R}"
            }
        }
    }
}