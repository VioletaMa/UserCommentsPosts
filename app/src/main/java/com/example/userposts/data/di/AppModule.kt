package com.example.userposts.data.di

import androidx.viewbinding.BuildConfig
import com.example.userposts.data.remote.PostsApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val READ_TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideHTTPClientAuthenticator(): OkHttpClient = OkHttpClient.Builder().apply {
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }).build()
    }.build()

    @Provides
    @Singleton
    fun providePostsApi(
        okHttpClient: OkHttpClient
    ): PostsApi {
        return Retrofit.Builder()
            .baseUrl(PostsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()
            .create()
    }
}