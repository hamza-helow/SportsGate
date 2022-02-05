package com.souqApp.data.addresses

import com.souqApp.data.addresses.remote.AddressApi
import com.souqApp.data.addresses.remote.AddressRepositoryImpl
import com.souqApp.domain.addresses.AddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AddressesModule {

    @Provides
    @Singleton
    fun addressApiProvide(retrofit: Retrofit): AddressApi {
        return retrofit.create(AddressApi::class.java)
    }

    @Provides
    @Singleton
    fun addressRepositoryProvide(addressApi: AddressApi): AddressRepository {
        return AddressRepositoryImpl(addressApi)
    }
}