package remote

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import remote.dto.*

interface PanelService {
    suspend fun shot(shotRequest: ShotRequest) : ShotResponse

    suspend fun shots(shotsRequest: ShotsRequest) : ShotsResponse

    companion object {
        fun create(httpClient: HttpClient): PanelServiceImpl{
            return PanelServiceImpl(
                client = httpClient
            )
        }
    }
}