package components

import data.HeaderData
import data.URLS
import react.*
import react.RBuilder
import react.RProps
import react.dom.*



external interface HeaderProps : RProps{
    var headerData: HeaderData
    var urls: URLS
}

val Header: FunctionalComponent<HeaderProps> = functionalComponent { props ->
    header("header"){
        div("header__logo"){
            img{
                attrs.src = props.urls.headerLogoURLKt
            }
            img{
                attrs.src = props.urls.headerLogoURLSpring
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
    attrs.urls = URLS()
    attrs.headerData = HeaderData()
}