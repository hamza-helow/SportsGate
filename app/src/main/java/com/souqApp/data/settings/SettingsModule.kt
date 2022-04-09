package com.souqApp.data.settings

import com.souqApp.data.settings.remote.SettingsApi
import com.souqApp.domain.settings.SettingsRepository
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
class SettingsModule {

    @Provides
    @ViewModelScoped
    fun settingsApiProvide(retrofit: Retrofit): SettingsApi {
        return retrofit.create(SettingsApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun settingsRepositoryProvide(settingsApi: SettingsApi): SettingsRepository {
        return SettingsRepositoryImpl(settingsApi)
    }

}