package com.souqApp.data.search

import com.souqApp.data.search.remote.SearchApi
import com.souqApp.domain.search.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {
    @Provides
    @Singleton
    fun searchApiProvide(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun searchRepositoryProvide(searchApi: SearchApi): SearchRepository {
        return SearchRepositoryImpl(searchApi)
    }
}