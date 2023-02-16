package data

enum class PanelStateOptions (val color: String){
    NOT_REQUESTED("grey"),
    HIT("green"),
    MISSED("red"),
    TABLE_LOADED("white"),
    TABLE_FAILED("black")
    // not needed yet

}