package com.souqApp.data.login

import com.souqApp.data.login.remote.api.LoginApi
import com.souqApp.data.login.repository.LoginRepositoryImpl
import com.souqApp.domain.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Provides
    @Singleton
    fun loginApiProvide(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun loginRepositoryProvide(loginApi: LoginApi): LoginRepository {
        return LoginRepositoryImpl(loginApi)
    }

}