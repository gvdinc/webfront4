package components

import data.HeaderData
import react.*
import react.dom.div
import react.dom.header
import react.dom.img
import react.dom.p
import remote.URLS


external interface HeaderProps : RProps{
    var headerData: HeaderData
}

val Header: FunctionalComponent<HeaderProps> = functionalComponent { props ->
    header("header"){
        div("header__logo"){
            img{
                attrs.src = URLS.HEADER_LOGO_URL_KT
            }
            img{
                attrs.src = URLS.HEADER_LOGO_URL_SPRING
            }
        }
        div("header__fio"){
            p{+ props.headerData.student}
        }
        div("header__group"){
            p{+ props.headerData.group}
        }
        div("header__variant"){
            p{+ props.headerData.variant}
        }
    }
}

fun RBuilder.myHeader() = child(Header) {
    attrs.headerData = HeaderData()
}