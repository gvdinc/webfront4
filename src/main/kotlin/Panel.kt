import components.mySVG
import data.Credentials
import data.PanelStateOptions
import io.ktor.client.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import remote.PanelService
import remote.dto.*
import kotlin.math.round
import kotlin.random.Random


external interface PanelProps: RProps{
    var exitFunction : () -> Unit
    var httpClient: HttpClient
    var credentials: Credentials
    var coordinates: ShotRequest
}

data class PanelState(
    var panelState: PanelStateOptions,
    val coords: ShotRequest,
    var shotList: List<ShotsResponseElement>? = null
) : RState

class Panel(props: PanelProps): RComponent<PanelProps, PanelState>(props) {
    private val panelService: PanelService = PanelService.create(props.httpClient)
    init {
        state = PanelState(
            PanelStateOptions.NOT_REQUESTED, 
            ShotRequest(props.credentials.login, props.credentials.password),
            null
        )
    }

    override fun RBuilder.render() {
        style("background-color: ${state.panelState.color}")

        
        div("panel_wrapper") {
            button(classes = "user_escape") {
                +"exit"
                attrs {
                    onClickFunction = {
                        props.exitFunction()
                    }
                }
            }
            
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
                                setState(PanelState(state.panelState, props.coordinates, state.shotList))
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
                                setState(PanelState(state.panelState, props.coordinates, state.shotList))
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
                                setState(PanelState(state.panelState, props.coordinates, state.shotList))
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
                                setState(PanelState(state.panelState, props.coordinates, state.shotList))
                            }
                        }
                    }
                }
                button(classes = "shot_button") {
                    +"Отправить"
                    attrs.onClickFunction = {
                        if (state.coords.valid()) shotRequestPOST(state.coords)
                    }
                }
                p {
                    +"x:${state.coords.x} y:${state.coords.y} R:${state.coords.R}"
                }
            }
            

            div("datatable") {

                table {
                    thead {
                        tr("table_head") {
                            th { +"Дата (Д Ч:М:С)" }
                            th { +"Время выполнения (ns)" }
                            th { +"В области?" }
                            th { +"Координата X" }
                            th { +"Координата Y" }
                            th { +"Масштаб (R)" }
                        }
                    }
                    tbody {
                        //for start
                        if (state.shotList != null) {
                            for (shot in state.shotList!!) {
                                th { +shot.datetime }
                                th { +shot.processing_time.toString() }
                                th { +shot.hit.toString() }
                                th { +shot.x.toString() }
                                th { +shot.y.toString() }
                                th { +shot.R.toString() }
                            }
                        }
                        //for end
                    }
                }
                // request button
                button { + "Загрузить историю"
                    attrs.onClickFunction = {shotsTablePOST(shotsRequest = ShotsRequest(
                        props.credentials.login, props.credentials.password
                    ))}
                }
            }
        }

    }

    private fun logoutPOST() {

    }

    private fun shotRequestPOST(shotRequest: ShotRequest) {
        console.log("sending request: $shotRequest")
        var job = CoroutineScope(Dispatchers.Main).launch {
            try {
                val response: ShotResponse = panelService.shot(shotRequest)
                if (response.hit) setState(PanelState(panelState = PanelStateOptions.HIT, state.coords, state.shotList))
                else {
                    setState(PanelState(panelState = PanelStateOptions.MISSED, state.coords, state.shotList))
                }
            } catch (e: IOException) {
                console.log(e.message)
            }
        }
    }

    private fun shotsTablePOST(shotsRequest: ShotsRequest) {
        console.log("requesting table data (shots)")
        var job = CoroutineScope(Dispatchers.Main).launch {
            try {
                val response: ShotsResponse = panelService.shots(shotsRequest)
                if (response.mass != null && response.mass?.isNotEmpty() == true) {
                    setState(PanelState(panelState = PanelStateOptions.TABLE_LOADED, state.coords, response.mass))
                }
                else{
                    setState(PanelState(panelState = PanelStateOptions.TABLE_FAILED, state.coords,null))
                }
            } catch (e: IOException) {
                console.error(e.message)
                setState(PanelState(panelState = PanelStateOptions.TABLE_FAILED, state.coords,null))
            }
        }
        // TODO : вызов ф-ции для отображения точек на дисплее
    }
}
