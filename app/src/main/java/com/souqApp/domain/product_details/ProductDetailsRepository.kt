package com.souqApp.domain.product_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {
    suspend fun productDetails(productID: Int): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsEntity>>>

    suspend fun addOrRemoveProduct(productId: Int): Flow<Boolean>

    suspend fun addProductToCart(productId: Int): Flow<Boolean>
}