package com.souqApp.domain.product_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.*
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {

    suspend fun productDetails(productID: Int): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>>

    suspend fun addOrRemoveProductToFavorite(
        productId: Int,
        combinationId: Int?
    ): Flow<BaseResult<AddToFavoriteResponse, WrappedResponse<AddToFavoriteResponse>>>

    suspend fun addProductToCart(
        productId: Int,
        combinationId: Int?
    ): Flow<BaseResult<AddProductToCartEntity, WrappedResponse<AddProductToCartResponse>>>

    suspend fun getVariationProductPriceInfo(
        productId: Int,
        label: String
    ): Flow<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>>
}