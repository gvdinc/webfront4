package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShotResponse (
        val hit: Boolean, // true или false
        val datetime: String, //в формате ISO 8601
        val processing_time_nano: Int,//в наносекундах
        val wrong_type: List<String>,//массив с ["x", "y", "R"] - смотря что имеет неверный тип (не Double)
)