package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShotRequest(
    var x: Double? = null,
    var y: Double? = null,
    var R: Double? = null

) {
    override fun toString(): String {
        return "ShotRequest(x=$x, y=$y, R=$R)"
    }

    fun findMistakes(): String{
        if (x == null) return "No x coordinate"
        if (y == null) return "No y coordinate"
        return if (R == null) "No R coordinate"
        else "unclassified mistake" // success
    }

    fun valid(): Boolean{
        if (x == null) return false
        if (y == null) return false
        return (R != null)
    }
}
