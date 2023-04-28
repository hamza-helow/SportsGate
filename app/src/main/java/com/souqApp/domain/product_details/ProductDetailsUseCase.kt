package com.souqApp.domain.product_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.AddToFavoriteResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductDetailsUseCase @Inject constructor(private val productDetailsRepository: ProductDetailsRepository) {

    suspend fun productDetails(productID: Int): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> {
        return productDetailsRepository.productDetails(productID)
    }

    suspend fun addOrRemoveProduct(productId: Int, combinationId: Int?): Flow<BaseResult<AddToFavoriteResponse, WrappedResponse<AddToFavoriteResponse>>>{
        return productDetailsRepository.addOrRemoveProductToFavorite(productId, combinationId)
    }

    suspend fun addProductToCart(productId: Int, combinationId: Int?): Flow<Boolean> {
        return productDetailsRepository.addProductToCart(productId, combinationId)
    }
}