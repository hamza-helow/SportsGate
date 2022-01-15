package com.souqApp.data.profile

import com.souqApp.data.profile.remote.api.ProfileApi
import com.souqApp.domain.profile.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProfileModule {

    @Provides
    @Singleton
    fun apiProfileProvide(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun profileRepositoryProvide(profileApi: ProfileApi): ProfileRepository {
        return ProfileRepositoryImpl(profileApi)
    }
}