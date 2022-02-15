package com.souqApp.data.contact_us

import com.souqApp.data.contact_us.remote.ContactUsApi
import com.souqApp.domain.contact_us.ContactUsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ContactUsModule {

    @Provides
    @Singleton
    fun contactUsApiProvide(retrofit: Retrofit): ContactUsApi {
        return retrofit.create(ContactUsApi::class.java)
    }

    @Provides
    @Singleton
    fun contactUsRepository(contactUsApi: ContactUsApi): ContactUsRepository {
        return ContactUsRepositoryImpl(contactUsApi)
    }
}