package com.souqApp.data.common.module

import com.souqApp.BuildConfig
import com.souqApp.data.common.utlis.RequestInterceptor
import com.souqApp.infra.utils.CONNECT_TIMEOUT
import com.souqApp.infra.utils.READ_TIMEOUT
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.infra.utils.WRITE_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
                baseUrl(BuildConfig.API_BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(okHttpClient)
            }.build()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
                this.addInterceptor(interceptor)
            }
        }.build()
    }


    @Provides
    @Singleton
    fun provideInterceptor(sharedPrefs: SharedPrefs): RequestInterceptor {
        return RequestInterceptor(sharedPrefs)
    }

}