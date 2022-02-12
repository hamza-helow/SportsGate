package com.souqApp.data.orders

import com.souqApp.data.orders.remote.OrdersApi
import com.souqApp.data.orders.remote.OrdersRepositoryImpl
import com.souqApp.domain.orders.OrdersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OrdersModule {

    @Provides
    @Singleton
    fun ordersApiProvide(retrofit: Retrofit): OrdersApi {
        return retrofit.create(OrdersApi::class.java)
    }

    @Provides
    @Singleton
    fun ordersRepositoryProvide(ordersApi: OrdersApi): OrdersRepository {
        return OrdersRepositoryImpl(ordersApi)
    }
}