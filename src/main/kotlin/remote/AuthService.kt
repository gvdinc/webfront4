package remote

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import remote.dto.LoginRequest
import remote.dto.LoginResponse
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse

interface AuthService {
    suspend fun registerRequest(registerRequest: RegisterRequest): RegisterResponse
    suspend fun loginRequest(loginRequest: LoginRequest): LoginResponse
    // TODO : add  suspend fun logout()
    companion object {
        fun create(httpClient: HttpClient): AuthService{
            return AuthServiceImpl(
                client = httpClient
            )
        }
    }
}