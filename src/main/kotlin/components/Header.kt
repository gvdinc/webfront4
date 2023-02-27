package components

import data.Variant
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.div
import react.dom.header
import react.dom.img
import react.dom.p
import remote.URLS


external interface HeaderProps : RProps{
    var changeVariantFun: () -> Unit
    var variant: Variant
}

val Header: FunctionalComponent<HeaderProps> = functionalComponent { props ->
    header("header"){
        attrs.id = "header"
        div("header__logo"){
            img{
                attrs.src = URLS.HEADER_LOGO_URL_KT
            }
            img{
                attrs.src = URLS.HEADER_LOGO_URL_SPRING
            }
            img{
                attrs.src = URLS.TOY_STORY_LOGO
            }
        }
        div("header__fio"){
            p{+ props.variant.fullName}
            attrs.onClickFunction = {
                props.changeVariantFun()
            }
        }
        div("header__group"){
            p{+ props.variant.group}
        }
        div("header__variant"){
            p{+ props.variant.variantNumber}
        }
    }
}

fun RBuilder.myHeader(variant: Variant, changeVariantFun: () -> Unit) = child(Header) {
    attrs.variant = variant
    attrs.changeVariantFun = changeVariantFun
}