package remote

import io.ktor.client.*
import remote.dto.LoginRequest
import remote.dto.LoginResponse
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse

class PanelServiceImpl (private val client: HttpClient) : AuthService{
    override suspend fun registerRequest(registerRequest: RegisterRequest): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun loginRequest(loginRequest: LoginRequest): LoginResponse {
        TODO("Not yet implemented")
    }

}