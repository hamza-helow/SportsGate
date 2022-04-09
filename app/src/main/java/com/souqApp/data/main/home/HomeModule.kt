package com.souqApp.data.main.home

import com.souqApp.data.main.home.remote.HomeApi
import com.souqApp.data.main.home.remote.HomeRepositoryImpl
import com.souqApp.domain.main.home.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
class HomeModule {

    @Provides
    @ViewModelScoped
    fun homeApiProvide(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun homeRepositoryProvide(homeApi: HomeApi): HomeRepository {
        return HomeRepositoryImpl(homeApi)
    }

}