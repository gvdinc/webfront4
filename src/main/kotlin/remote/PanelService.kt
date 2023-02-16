package remote

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import remote.dto.*

interface PanelService {
    suspend fun shot(shotRequest: ShotRequest) : ShotResponse

    suspend fun shots() : ShotsResponse

    companion object {
        fun create(): PanelServiceImpl{
            return PanelServiceImpl(
                client = HttpClient(Js) {
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                        })
                    }
                }
            )
        }
    }
}