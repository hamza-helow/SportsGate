package com.souqApp.data.register

import com.souqApp.data.register.remote.api.RegisterApi
import com.souqApp.data.register.repositroy.RegisterRepositoryImpl
import com.souqApp.domain.register.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RegisterModule {

    @Provides
    @Singleton
    fun registerApiProvide(retrofit: Retrofit): RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    @Provides
    @Singleton
    fun registerRepositoryProvide(registerApi: RegisterApi): RegisterRepository {
        return RegisterRepositoryImpl(registerApi)
    }

}