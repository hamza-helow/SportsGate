package com.souqApp.data.change_password

import com.souqApp.data.change_password.remote.ChangePasswordApi
import com.souqApp.data.change_password.remote.ChangePasswordRepositoryImpl
import com.souqApp.domain.change_password.ChangePasswordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChangePasswordModule {

    @Provides
    @Singleton
    fun changePasswordApiProvide(retrofit: Retrofit): ChangePasswordApi {
        return retrofit.create(ChangePasswordApi::class.java)
    }

    @Provides
    @Singleton
    fun changePasswordRepositoryProvide(changePasswordApi: ChangePasswordApi): ChangePasswordRepository {
        return ChangePasswordRepositoryImpl(changePasswordApi)
    }

}
