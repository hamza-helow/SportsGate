package com.souqApp.data.sub_categories

import com.souqApp.data.sub_categories.remote.SubCategoriesApi
import com.souqApp.domain.sub_categories.SubCategoriesRepository
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
class SubCategoriesModule {

    @Provides
    @ViewModelScoped
    fun subCategoriesApiProvide(retrofit: Retrofit): SubCategoriesApi {
        return retrofit.create(SubCategoriesApi::class.java)
    }


    @Provides
    @ViewModelScoped
    fun subCategoriesRepositoryProvide(subCategoriesApi: SubCategoriesApi): SubCategoriesRepository {
        return SubCategoriesRepositoryImpl(subCategoriesApi)
    }

}