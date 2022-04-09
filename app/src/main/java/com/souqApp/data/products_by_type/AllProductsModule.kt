package com.souqApp.data.products_by_type

import com.souqApp.data.products_by_type.remote.ProductsByTypeApi
import com.souqApp.data.products_by_type.remote.ProductsByTypeByTypeRepositoryImpl
import com.souqApp.domain.products_by_type.ProductsByTypeRepository
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
class AllProductsModule {

    @Provides
    @ViewModelScoped
    fun allProductsApiProvide(retrofit: Retrofit): ProductsByTypeApi {
        return retrofit.create(ProductsByTypeApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun allProductsRepositoryProvide(productsByTypeApi: ProductsByTypeApi): ProductsByTypeRepository {
        return ProductsByTypeByTypeRepositoryImpl(productsByTypeApi)
    }

}