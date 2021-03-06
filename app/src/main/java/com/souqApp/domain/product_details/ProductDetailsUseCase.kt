package com.souqApp.domain.product_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductDetailsUseCase @Inject constructor(private val productDetailsRepository: ProductDetailsRepository) {

    suspend fun productDetails(productID: Int): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsEntity>>> {
        return productDetailsRepository.productDetails(productID)
    }

    suspend fun addOrRemoveProduct(productId: Int): Flow<Boolean> {
        return productDetailsRepository.addOrRemoveProduct(productId)
    }

    suspend fun addProductToCart(productId: Int): Flow<Boolean> {
        return productDetailsRepository.addProductToCart(productId)
    }
}