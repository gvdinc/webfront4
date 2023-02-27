package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShotRequest(
    val login: String,
    val password_hash: String,
    var x: Double? = null,
    var y: Double? = null,
    var R: Double? = null,
    val area_id: String,
) {
    override fun toString(): String {
        return "ShotRequest(x=$x, y=$y, R=$R, area_id=$area_id)"
    }

    fun findMistakes(): String{
        if (R == null) return "No R coordinate"
        if (x == null) return "No x coordinate"
        else if (y == null) return "No y coordinate"
        else if (R!! <= -5.0 || R!! >= 3.0) return "incorrect R"
        else return "correct" // success
    }

    fun valid(): Boolean{
        if (x == null) return false
        if (y == null) return false
        return (R != null)
    }
}
