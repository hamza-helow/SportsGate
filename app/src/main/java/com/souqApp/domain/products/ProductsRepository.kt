package com.souqApp.domain.products

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.data.products.remote.dto.AddProductToCartResponse
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.data.products.remote.dto.ProductDetailsEntity
import com.souqApp.data.products.remote.dto.ProductDetailsResponse
import com.souqApp.data.products.remote.dto.VariationProductPriceInfoResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.AddProductToCartEntity
import com.souqApp.domain.products.entity.VariationProductPriceInfoEntity
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProducts(
        type: Int? = null,
        page: Int? = null,
        search: String? = null,
        tag: Int? = null,
        promo: Int? = null,
        recommended: Int? = null,
    ): WrappedListResponse<ProductEntity>

    suspend fun productDetails(productID: Int?): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>>

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


    suspend fun getFavoriteProducts(): WrappedListResponse<ProductEntity>
}