package xyz.droidev.notally.data.model.response

data class AuthResponse(
    val user: User,
    val accessToken: String
)