package com.souqApp.data.sub_categories

import com.souqApp.data.sub_categories.remote.SubCategoriesApi
import com.souqApp.domain.sub_categories.SubCategoriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SubCategoriesModule {

    @Provides
    @Singleton
    fun subCategoriesApiProvide(retrofit: Retrofit): SubCategoriesApi {
        return retrofit.create(SubCategoriesApi::class.java)
    }


    @Provides
    @Singleton
    fun subCategoriesRepositoryProvide(subCategoriesApi: SubCategoriesApi): SubCategoriesRepository {
        return SubCategoriesRepositoryImpl(subCategoriesApi)
    }

}