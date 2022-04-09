package com.souqApp.data.login

import com.souqApp.data.login.remote.api.LoginApi
import com.souqApp.data.login.repository.LoginRepositoryImpl
import com.souqApp.domain.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class LoginModule {

    @Provides
    @ViewModelScoped
    fun loginApiProvide(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun loginRepositoryProvide(loginApi: LoginApi): LoginRepository {
        return LoginRepositoryImpl(loginApi)
    }

}