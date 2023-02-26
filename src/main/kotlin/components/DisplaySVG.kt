package components

import kotlinx.html.HTMLTag
import react.RBuilder
import react.ReactElement
import react.dom.RDOMBuilder
import react.dom.svg
import react.dom.tag
import remote.dto.ShotsResponseElement

// a custom tag builder, reuses the tag(...) function from kotlin-react and HTMLTag from kotlinx.html
inline fun RBuilder.custom(tagName: String, block: RDOMBuilder<HTMLTag>.() -> Unit): ReactElement = tag(block) {
    HTMLTag(tagName, it, mapOf(), null, inlineTag = true, emptyTag = false) // I don't know yet what the last 3 params mean... to lazy to look it up
}

// example use
fun RBuilder.mySVG(R: Double, mass: List<ShotsResponseElement>? = null) {
    val pointHitColor = "#0023c7"
    val pointMissedColor = "#b40000"

    svg(classes = "svg_img") {
        attrs["width"] = 200.0
        attrs["height"] = 200.0
        attrs["viewBox"] = "0 0 ${2*R} ${2*R}"

        // axis
        custom("line"){ // Oy
            attrs["className"] = "axis_line"
            attrs["x1"] = "$R"
            attrs["y1"] = "0"
            attrs["x2"] = "$R"
            attrs["y2"] = "${2*R}"
        }
        custom("line"){ // Ox
            attrs["className"] = "axis_line"
            attrs["x1"] = "0"
            attrs["y1"] = "$R"
            attrs["x2"] = "${2*R}"
            attrs["y2"] = "$R"
        }
        // axis-arrows
        custom("polygon"){
            attrs["points"] = "$R,${0} ${1.05*R},${0.1*R} ${0.95*R},${0.1*R}"
            //attrs["fill"] = "#000000"
        }
        custom("polygon"){
            attrs["points"] = "${2*R},$R ${1.9*R},${1.05*R} ${1.9*R},${0.95*R}"
            //attrs["fill"] = "#000000"
        }

        if (mass != null){
            for (shot in mass){
                val scale = R / shot.R // if shot on 30 and scale is 5 then scale = 0.167
                if (shot.hit) custom("circle"){
                    attrs["cx"] = (shot.x + shot.R)* scale
                    attrs["cy"] = (shot.R - shot.y) * scale
                    attrs["r"] = "${0.04*R}"
                    attrs["className"] = "point, point_hit"
                    attrs["fill"] = pointHitColor
                }
                else custom("circle"){
                    attrs["cx"] = (shot.x + shot.R)* scale
                    attrs["cy"] = (shot.R - shot.y) * scale
                    attrs["r"] = "${0.04*R}"
                    attrs["className"] = "point, point_missed"
                    attrs["fill"] = pointMissedColor
                }


            }
        }


    }
}