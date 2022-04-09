package com.souqApp.data.main.categories

import com.souqApp.data.main.categories.remote.CategoriesApi
import com.souqApp.domain.main.categories.CategoriesRepository
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
class CategoriesModule {

    @Provides
    @ViewModelScoped
    fun categoriesApiProvide(retrofit: Retrofit): CategoriesApi {
        return retrofit.create(CategoriesApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun repositoryCategoriesProvide(categoriesApi: CategoriesApi): CategoriesRepository {
        return CategoriesRepositoryImpl(categoriesApi)
    }

}