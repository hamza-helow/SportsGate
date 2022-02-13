package com.souqApp.data.order_details

import com.souqApp.data.order_details.remote.OrderDetailsApi
import com.souqApp.domain.order_details.OrderDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OrderDetailsModule {

    @Provides
    @Singleton
    fun orderDetailsApiProvide(retrofit: Retrofit): OrderDetailsApi {
        return retrofit.create(OrderDetailsApi::class.java)
    }

    @Provides
    @Singleton
    fun orderDetailsRepositoryProvide(orderDetailsApi: OrderDetailsApi) :OrderDetailsRepository {
        return OrderDetailsRepositoryImpl(orderDetailsApi)
    }
}