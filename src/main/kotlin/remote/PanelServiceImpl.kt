package remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import remote.dto.ShotRequest
import remote.dto.ShotResponse
import remote.dto.ShotsRequest
import remote.dto.ShotsResponse

class PanelServiceImpl (private val client: HttpClient) : PanelService{
    override suspend fun shot(shotRequest: ShotRequest): ShotResponse {
        val response: ShotResponse = client.post(URLS.SHOT){
            contentType(ContentType.Application.Json)
            setBody(shotRequest)
        }.body()
        return response
    }

    override suspend fun shots(shotsRequest: ShotsRequest): ShotsResponse {
        val response: ShotsResponse = client.post(URLS.SHOTS){
            contentType(ContentType.Application.Json)
            setBody(shotsRequest)
        }.body()
        return response
    }


}