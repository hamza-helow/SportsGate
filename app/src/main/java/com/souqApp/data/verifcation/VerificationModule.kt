package com.souqApp.data.verifcation

import com.souqApp.data.verifcation.remote.VerificationApi
import com.souqApp.domain.verifcation.VerificationRepository
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
class VerificationModule {

    @Provides
    @ViewModelScoped
    fun verificationApi(retrofit: Retrofit): VerificationApi {
        return retrofit.create(VerificationApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun verificationRepository(verificationApi: VerificationApi): VerificationRepository {
        return VerificationRepositoryImpl(verificationApi)
    }
}