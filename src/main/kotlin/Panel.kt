import components.mySVG
import data.Credentials
import data.PanelStateOptions
import io.ktor.client.*
import io.ktor.utils.io.errors.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.MouseEvent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import remote.PanelService
import remote.dto.*
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.random.Random

val testArr: List<ShotsResponseElement> = listOf(
    ShotsResponseElement(x=1.0, y=2.0, R=3.0, hit = true, datetime = "2023-02-16 19:56:58.129000", processing_time_nano = 16600),
    ShotsResponseElement(x=0.2, y=0.0, R=1.0, hit = false, datetime = "2023-02-16 20:07:14.952000", processing_time_nano = 4100),
    ShotsResponseElement(x=-2.0, y=-4.0, R=8.0, hit = true, datetime = "2023-02-17 09:14:10.019000", processing_time_nano = 16800)
)

external interface PanelProps: RProps{
    var exitFunction : () -> Unit
    var httpClient: HttpClient
    var credentials: Credentials
    var coordinates: ShotRequest
}

data class PanelState(
    var panelState: PanelStateOptions,
    val coords: ShotRequest,
    var shotList: List<ShotsResponseElement>? = null,
    var displaySize: Double = window.innerWidth.toDouble()
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
        div("panel_wrapper") {

            div("display_wrapper") {
                div("display") {
                    div("svg_grid") {
                        mySVG(state.coords.R ?: 5.0, state.shotList)

                        //attrs.onClickFunction
                        attrs {
                            id = "svg_div"
                            onClickFunction = { event ->
                                // properties
                                val mouseLeft = (event.unsafeCast<MouseEvent>()).clientX
                                val mouseTop = (event.unsafeCast<MouseEvent>()).clientY
                                val width: Double = (event.currentTarget as HTMLDivElement).offsetWidth.toDouble()
                                val height: Double = (event.currentTarget as HTMLDivElement).offsetHeight.toDouble()
                                val deltaLeft = (event.currentTarget as HTMLDivElement).offsetLeft
                                val deltaTop = (event.currentTarget as HTMLDivElement).offsetTop
                                val headerElement: Element? = document.getElementById("header")
                                val headerHeight: Int = (headerElement?.clientHeight ?: 32) + 10
                                // relative values
                                val mouseLeftRelative: Double = (mouseLeft - deltaLeft).toDouble()
                                val mouseTopRelative: Double = (mouseTop - deltaTop - headerHeight).toDouble()
                                val r: Double = props.coordinates.R ?: 5.0
                                // calculation
                                val x: Double = ((2.0 * mouseLeftRelative * r / width - r)*10).roundToInt().toDouble()/10
                                val y: Double = ((r - 2.0 * mouseTopRelative * r / height)*10).roundToInt().toDouble()/10

                                if (props.coordinates.R != null) {
                                    props.coordinates.x = x
                                    props.coordinates.y = y
                                    setState(PanelState(state.panelState, props.coordinates, state.shotList))
                                }
                            }
                        }
                    }
                }

                p { +state.coords.findMistakes() }
                div("Block") {
                    input(classes = "inputs_x", name = "x_input") {
                        attrs {
                            placeholder = "X"
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
                            placeholder = "Y"
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
                            placeholder = "R"
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
                div("buttons") {
                    button(classes = "shot_button") {
                        +"Отправить"
                        attrs.onClickFunction = {
                            if (state.coords.valid()) shotRequestPOST(state.coords)
                        }
                    }
                    button(classes = "user_escape") {
                        +"exit"
                        attrs {
                            onClickFunction = {
                                props.exitFunction()
                            }
                        }
                    }
                }
                p {
                    +"x:${state.coords.x} y:${state.coords.y} R:${state.coords.R}"
                }
            }


            div("datatable") {
                // request button
                button { + "Загрузить историю"
                    attrs.onClickFunction = {shotsTablePOST(shotsRequest = ShotsRequest(
                        props.credentials.login, props.credentials.password
                    ))}
                    //setState(PanelState(state.panelState, props.coordinates, testArr)) // for testing
                }
                table {
                    thead {
                        tr("table_head") {
                            th { +"\uD83D\uDCC5" }
                            th { +"⌚" }
                            th { +"\uD83C\uDFAF" }
                            th { +"X" }
                            th { +"Y" }
                            th { +"R" }
                        }
                    }
                    tbody {
                        //for start
                        if (state.shotList != null) {
                            for (shot in state.shotList!!) {
                                tr {
                                    th { + regFormat(shot.datetime) }
                                    th { +( shot.processing_time_nano / 100).toString() }
                                    th { +shotHitSymbol(shot.hit) }
                                    th { +shot.x.toString() }
                                    th { +shot.y.toString() }
                                    th { +shot.R.toString() }
                                }
                            }
                        }
                        //for end
                    }
                }

            }
        }
    }

    private fun shotHitSymbol(hit: Boolean): String {
        return if (hit) "✅" else "❎" /*❌*/
    }

    private fun regFormat(datetime: String): String {
        val regEx = Regex(".*(?=:)")
        val formatted: MatchResult? = regEx.find(datetime.replace('-','.'))
        console.log("formatted to ${formatted?.value ?: ""}")
        return formatted?.value ?: ""
    }

    private fun logoutPOST() {

    }

    private fun shotRequestPOST(shotRequest: ShotRequest) {
        console.log("sending request: $shotRequest")
        CoroutineScope(Dispatchers.Main).launch {
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
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response: ShotsResponse = panelService.shots(shotsRequest)
                if (response.shots != null && response.shots?.isNotEmpty() == true) {
                    setState(PanelState(panelState = PanelStateOptions.TABLE_LOADED, state.coords, response.shots))
                }
                else{
                    setState(PanelState(panelState = PanelStateOptions.TABLE_FAILED, state.coords,null))
                }
            } catch (e: IOException) {
                console.error(e.message)
                setState(PanelState(panelState = PanelStateOptions.TABLE_FAILED, state.coords,null))
            }
        }
    }
}
