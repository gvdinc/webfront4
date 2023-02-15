package remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import remote.dto.LoginRequest
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse


class AuthServiceImpl( private val client: HttpClient
    ) : AuthService{

    override suspend fun registerRequest(registerRequest: RegisterRequest): RegisterResponse? {
        val response: RegisterResponse = client.post(URLS.REGISTER) {
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }.body()
        println("resister state is ${response.register_state}")
        return response
    }

    override suspend fun loginRequest(loginRequest: LoginRequest): Boolean {
        TODO("Not yet implemented")
    }

}
