package com.souqApp.data.notification

import com.souqApp.data.notification.remote.NotificationApi
import com.souqApp.domain.notification.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationModule {

    @Provides
    @Singleton
    fun notificationApiProvide(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @Singleton
    fun notificationRepositoryProvide(notificationApi: NotificationApi): NotificationRepository {
        return NotificationRepositoryImpl(notificationApi)
    }
}