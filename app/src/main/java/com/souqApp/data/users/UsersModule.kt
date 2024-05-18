package com.souqApp.data.users

import com.souqApp.data.users.remote.api.UsersApi
import com.souqApp.domain.users.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class UsersModule {

    @Provides
    @ViewModelScoped
    fun apiUsersProvide(retrofit: Retrofit): UsersApi {
        return retrofit.create(UsersApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun usersRepositoryProvide(usersApi: UsersApi): UsersRepository {
        return UsersRepositoryImpl(usersApi)
    }
}