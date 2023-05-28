package xyz.droidev.notally.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import xyz.droidev.notally.data.model.request.LoginBody
import xyz.droidev.notally.data.model.request.SignupBody
import xyz.droidev.notally.data.model.response.AuthResponse

interface UserApiService {

    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ) : Response<AuthResponse>

    @POST("user/register")
    suspend fun signup(
        @Body signupBody: SignupBody
    ) : Response<AuthResponse>

}