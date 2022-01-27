package com.souqApp.data.products_by_type

import com.souqApp.data.products_by_type.remote.ProductsByTypeApi
import com.souqApp.data.products_by_type.remote.ProductsByTypeByTypeRepositoryImpl
import com.souqApp.domain.products_by_type.ProductsByTypeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AllProductsModule {

    @Provides
    @Singleton
    fun allProductsApiProvide(retrofit: Retrofit): ProductsByTypeApi {
        return retrofit.create(ProductsByTypeApi::class.java)
    }

    @Provides
    @Singleton
    fun allProductsRepositoryProvide(productsByTypeApi: ProductsByTypeApi): ProductsByTypeRepository {
        return ProductsByTypeByTypeRepositoryImpl(productsByTypeApi)
    }

}