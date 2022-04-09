package com.souqApp.data.product_details

import com.souqApp.data.product_details.remote.ProductDetailsApi
import com.souqApp.domain.product_details.ProductDetailsRepository
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
class ProductDetailsModule {

    @Provides
    @ViewModelScoped
    fun productDetailsApiProvide(retrofit: Retrofit): ProductDetailsApi {
        return retrofit.create(ProductDetailsApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun productDetailsRepository(productDetailsApi: ProductDetailsApi): ProductDetailsRepository {
        return ProductDetailsRepositoryImpl(productDetailsApi)
    }
}