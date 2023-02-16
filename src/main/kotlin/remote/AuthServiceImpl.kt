package remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import remote.dto.LoginRequest
import remote.dto.LoginResponse
import remote.dto.RegisterRequest
import remote.dto.RegisterResponse


class AuthServiceImpl( private val client: HttpClient
    ) : AuthService{

    override suspend fun registerRequest(registerRequest: RegisterRequest): RegisterResponse {
        val response: RegisterResponse = try{client.post(URLS.REGISTER) {
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }.body()} catch (e: Exception){
            console.error("registration failed with status: ${e.message} ")
            return RegisterResponse(register_state = e.message.toString())
        }
        //println("resister state is ${response.register_state}")
        return response
    }

    override suspend fun loginRequest(loginRequest: LoginRequest): LoginResponse {
        val response: LoginResponse = try{client.post(URLS.LOGIN) {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()} catch (e: Exception){
            console.error("login failed with status: ${e.message} ")
            return LoginResponse(login_state = e.message.toString())
        }
        return response
    }

}
