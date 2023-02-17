import components.InputsBlock
import components.mySVG
import data.Credentials
import data.PanelStateOptions
import io.ktor.client.*
import io.ktor.utils.io.errors.*
import kotlinext.js.getOwnPropertyNames
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseMoveFunction
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import remote.PanelService
import remote.dto.*


external interface PanelProps: RProps{
    var exitFunction : () -> Unit
    var httpClient: HttpClient
    var credentials: Credentials
}

data class PanelState(var panelState: PanelStateOptions, var shotArray: List<ShotsResponseElement>?) : RState

class Panel(props: PanelProps): RComponent<PanelProps, PanelState>(props) {
    private val panelService: PanelService = PanelService.create(props.httpClient)
    init {
        state = PanelState(PanelStateOptions.NOT_REQUESTED, null)
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

            div("dashboard") {
                val inputs = child(InputsBlock::class) {
                    attrs.coordinates = ShotRequest(props.credentials.login, props.credentials.password)
                    attrs.requestFunction = (::shotRequestPOST)
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
                        if (state.shotArray != null) {
                            for (shot in state.shotArray!!) {
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
                if (response.hit) setState(PanelState(panelState = PanelStateOptions.HIT, state.shotArray))
                else {
                    setState(PanelState(panelState = PanelStateOptions.MISSED, state.shotArray))
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
                    setState(PanelState(panelState = PanelStateOptions.TABLE_LOADED, response.mass))
                }
                else{
                    setState(PanelState(panelState = PanelStateOptions.TABLE_FAILED, null))
                }
            } catch (e: IOException) {
                console.error(e.message)
                setState(PanelState(panelState = PanelStateOptions.TABLE_FAILED, null))
            }
        }
        // TODO : вызов ф-ции для отображения точек на дисплее
    }
}
