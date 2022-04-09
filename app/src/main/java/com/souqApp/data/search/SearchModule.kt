package com.souqApp.data.search

import com.souqApp.data.search.remote.SearchApi
import com.souqApp.domain.search.SearchRepository
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
class SearchModule {
    @Provides
    @ViewModelScoped
    fun searchApiProvide(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun searchRepositoryProvide(searchApi: SearchApi): SearchRepository {
        return SearchRepositoryImpl(searchApi)
    }
}