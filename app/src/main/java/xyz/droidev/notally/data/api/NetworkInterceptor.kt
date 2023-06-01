package xyz.droidev.notally.data.api

import okhttp3.Interceptor
import okhttp3.Response
import xyz.droidev.notally.data.local.prefrences.PreferenceManager
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: PreferenceManager

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}