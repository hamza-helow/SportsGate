package com.souqApp.data.create_password

import com.souqApp.BuildConfig
import com.souqApp.data.create_password.remote.CreatePasswordApi
import com.souqApp.domain.create_password.CreatePasswordRepository
import com.souqApp.infra.utils.CONNECT_TIMEOUT
import com.souqApp.infra.utils.READ_TIMEOUT
import com.souqApp.infra.utils.WRITE_TIMEOUT
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
class CreatePasswordModule {

    @Provides
    @Singleton
    fun createPasswordApiProvide(): CreatePasswordApi {
        val retrofit = Retrofit
            .Builder().apply {
                baseUrl(BuildConfig.API_BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(OkHttpClient.Builder().apply {
                    connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                }.build())
            }.build()

        return retrofit.create(CreatePasswordApi::class.java)
    }

    @Provides
    @Singleton
    fun createPasswordRepositoryProvide(createPasswordApi: CreatePasswordApi): CreatePasswordRepository {
        return CreatePasswordRepositoryImpl(createPasswordApi)
    }
}