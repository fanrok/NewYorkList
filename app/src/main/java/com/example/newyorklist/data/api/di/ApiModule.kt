package com.example.newyorklist.data.api.di

import com.example.newyorklist.BuildConfig
import com.example.newyorklist.data.api.ApiHelper
import com.example.newyorklist.data.api.ApiHelperImpl
import com.example.newyorklist.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Dmitriy Larin
 * Api module
 *
 * @constructor Create empty Api module
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    /**
     * Provide base url
     *
     */
    @Provides
    fun provideBaseUrl() = "https://api.nytimes.com"

    /**
     * Provide ok http client
     *
     */
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    /**
     * Provide retrofit
     *
     * @param okHttpClient
     * @param BASE_URL
     * @return
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    /**
     * Provide api service
     *
     * @param retrofit
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    /**
     * Provide api helper
     *
     * @param apiHelper
     * @return
     */
    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

}