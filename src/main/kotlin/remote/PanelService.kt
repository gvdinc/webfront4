package remote

import io.ktor.client.*
import remote.dto.ShotRequest
import remote.dto.ShotResponse
import remote.dto.ShotsRequest
import remote.dto.ShotsResponse

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