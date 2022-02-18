package com.souqApp.data.settings

import com.souqApp.data.settings.remote.SettingsApi
import com.souqApp.domain.settings.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {

    @Provides
    @Singleton
    fun settingsApiProvide(retrofit: Retrofit): SettingsApi {
        return retrofit.create(SettingsApi::class.java)
    }

    @Provides
    @Singleton
    fun settingsRepositoryProvide(settingsApi: SettingsApi): SettingsRepository {
        return SettingsRepositoryImpl(settingsApi)
    }

}