package com.souqApp.data.forgot_password

import com.souqApp.data.forgot_password.remote.api.ForgotPasswordApi
import com.souqApp.data.forgot_password.repoistory.ForgotPasswordRepositoryImpl
import com.souqApp.domain.forgot_password.ForgotPasswordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ForgotPasswordModule {

    @Provides
    @Singleton
    fun forgotPasswordApi(retrofit: Retrofit): ForgotPasswordApi {
        return retrofit.create(ForgotPasswordApi::class.java)
    }

    @Provides
    @Singleton
    fun forgotPasswordRepository(forgotPasswordApi: ForgotPasswordApi): ForgotPasswordRepository {
        return ForgotPasswordRepositoryImpl(forgotPasswordApi)
    }

}