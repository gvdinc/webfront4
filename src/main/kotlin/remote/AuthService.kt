package remote

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import remote.dto.LoginRequest
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse

interface AuthService {
    suspend fun registerRequest(registerRequest: RegisterRequest): RegisterResponse?
    suspend fun loginRequest(loginRequest: LoginRequest): Boolean

    companion object {
        fun create(): AuthService{
            return AuthServiceImpl(
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