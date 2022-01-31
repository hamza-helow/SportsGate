package com.souqApp.data.main.cart

import com.souqApp.data.main.cart.remote.CartApi
import com.souqApp.data.main.cart.remote.CartRepositoryImpl
import com.souqApp.domain.main.cart.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CartModule {

    @Provides
    @Singleton
    fun cartApiProvide(retrofit: Retrofit): CartApi {
        return retrofit.create(CartApi::class.java)
    }

    @Provides
    @Singleton
    fun cartRepositoryProvide(cartApi: CartApi): CartRepository {
        return CartRepositoryImpl(cartApi)
    }
}