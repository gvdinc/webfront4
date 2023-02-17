package remote

import io.ktor.client.*
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