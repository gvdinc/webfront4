package remote



object URLS {
    private const val port = 8080
    private const val serverUrl: String = "http://localhost:$port"

    // request urls
    const val REGISTER = "$serverUrl/register"
    const val LOGIN = "$serverUrl/login"
    const val LOGOUT = "$serverUrl/logout"
    const val SHOT = "$serverUrl/shot"
    const val SHOTS = "$serverUrl/shots"

    const val HEADER_LOGO_URL_KT = 
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Kotlin_logo.svg/2560px-Kotlin_logo.svg.png"
    const val HEADER_LOGO_URL_SPRING = 
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/800px-Spring_Framework_Logo_2018.svg.png"
}