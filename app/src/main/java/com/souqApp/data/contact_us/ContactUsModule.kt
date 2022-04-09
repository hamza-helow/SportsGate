package com.souqApp.data.contact_us

import com.souqApp.data.contact_us.remote.ContactUsApi
import com.souqApp.domain.contact_us.ContactUsRepository
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
class ContactUsModule {

    @Provides
    @ViewModelScoped
    fun contactUsApiProvide(retrofit: Retrofit): ContactUsApi {
        return retrofit.create(ContactUsApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun contactUsRepository(contactUsApi: ContactUsApi): ContactUsRepository {
        return ContactUsRepositoryImpl(contactUsApi)
    }
}