package com.souqApp.data.products

import com.souqApp.data.products.remote.ProductsApi
import com.souqApp.data.products.remote.ProductsRepositoryImpl
import com.souqApp.domain.products.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
class ProductsModule {

    @Provides
    @ViewModelScoped
    fun productsApiProvide(retrofit: Retrofit): ProductsApi {
        return retrofit.create(ProductsApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun productsRepositoryProvide(productsApi: ProductsApi): ProductsRepository {
        return ProductsRepositoryImpl(productsApi)
    }
}