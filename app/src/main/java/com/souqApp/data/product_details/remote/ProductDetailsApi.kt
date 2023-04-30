package com.souqApp.data.product_details.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductDetailsApi {
    @GET("v2/products/getProductDetails")
    suspend fun productDetails(@Query("id") productId: Int): WrappedResponse<ProductDetailsResponse>

    @POST("v2/users/favorites/addOrRemoveFavoriteProduct")
    suspend fun addOrRemoveProductToFavorite(
        @Query("product_id") productId: Int,
        @Query("combination_id") combinationId: Int?
    ): WrappedResponse<AddToFavoriteResponse>

    @POST("v2/users/carts/addProductToCart")
    suspend fun addProductToCart(
        @Query("product_id") productId: Int,
        @Query("combination_id") combinationId: Int?,
        @Query("qty") count: Int = 1
    ): WrappedResponse<AddProductToCartResponse>

    @GET("v2/products/getVariationProductPriceInfo")
    suspend fun getVariationProductPriceInfo(
        @Query("id") productId: Int,
        @Query("label") label: String
    ): WrappedResponse<VariationProductPriceInfoResponse>

}

