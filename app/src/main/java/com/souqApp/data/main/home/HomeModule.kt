package com.souqApp.data.main.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.main.home.remote.HomeApi
import com.souqApp.data.main.home.remote.HomeRepositoryImpl
import com.souqApp.domain.main.home.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class HomeModule {

    @Provides
    @Singleton
    fun homeApiProvide(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun homeRepositoryProvide(homeApi: HomeApi): HomeRepository {
        return HomeRepositoryImpl(homeApi)
    }

}