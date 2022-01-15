package com.souqApp.data.common.module

import com.souqApp.BuildConfig
import com.souqApp.data.common.utlis.RequestInterceptor
import com.souqApp.infra.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder().apply {
                baseUrl(BuildConfig.API_BASE_URL_DEV)
                addConverterFactory(GsonConverterFactory.create())
                client(okHttpClient)
            }.build()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
        }.build()
    }


    @Provides
    @Singleton
    fun provideInterceptor(sharedPrefs: SharedPrefs): RequestInterceptor {
        return RequestInterceptor(sharedPrefs)
    }

}