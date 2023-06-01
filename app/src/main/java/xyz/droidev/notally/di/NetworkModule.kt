package xyz.droidev.notally.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.droidev.notally.data.api.NetworkInterceptor
import xyz.droidev.notally.data.api.NotesApiService
import xyz.droidev.notally.data.api.UserApiService
import xyz.droidev.notally.util.Constants.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserApiService {
        return retrofitBuilder.build().create(UserApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: NetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesNoteAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): NotesApiService{
        return retrofitBuilder.client(okHttpClient).build().create(NotesApiService::class.java)
    }

}