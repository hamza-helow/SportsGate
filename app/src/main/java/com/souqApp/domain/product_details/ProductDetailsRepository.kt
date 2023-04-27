package com.souqApp.domain.product_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.data.product_details.remote.VariationProductPriceInfoResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {

    suspend fun productDetails(productID: Int): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>>

    suspend fun addOrRemoveProduct(productId: Int): Flow<Boolean>

    suspend fun addProductToCart(productId: Int, combinationId: Int?): Flow<Boolean>

    suspend fun getVariationProductPriceInfo(
        productId: Int,
        label: String
    ): Flow<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>>
}