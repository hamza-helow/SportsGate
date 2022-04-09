package com.souqApp.data.notification

import com.souqApp.data.notification.remote.NotificationApi
import com.souqApp.domain.notification.NotificationRepository
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
class NotificationModule {

    @Provides
    @ViewModelScoped
    fun notificationApiProvide(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun notificationRepositoryProvide(notificationApi: NotificationApi): NotificationRepository {
        return NotificationRepositoryImpl(notificationApi)
    }
}