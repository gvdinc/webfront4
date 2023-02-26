package remote



object URLS {
    private const val port = 8081
    private const val serverUrl: String = "http://localhost:$port"

    // request urls
    const val REGISTER = "$serverUrl/register"
    const val LOGIN = "$serverUrl/login"
    const val LOGOUT = "$serverUrl/logout"
    const val SHOT = "$serverUrl/shot"
    const val SHOTS = "$serverUrl/shots"

    // images
    const val TOY_STORY_LOGO =
        "https://vignette.wikia.nocookie.net/logopedia/images/4/4e/Toy_story_logo_horizontal_by_framerater-d9bl9ow.png/revision/latest?cb=20161227115201"
    const val HEADER_LOGO_URL_KT = 
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Kotlin_logo.svg/2560px-Kotlin_logo.svg.png"
    const val HEADER_LOGO_URL_SPRING = 
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/800px-Spring_Framework_Logo_2018.svg.png"
}