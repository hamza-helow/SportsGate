package com.souqApp.data.products.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.data.products.remote.dto.AddProductToCartResponse
import com.souqApp.data.products.remote.dto.AddToFavoriteResponse
import com.souqApp.data.products.remote.dto.ProductDetailsResponse
import com.souqApp.data.products.remote.dto.VariationProductPriceInfoResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductsApi {
    @GET("v2/products/getProductDetails")
    suspend fun productDetails(@Query("id") productId: Int?): WrappedResponse<ProductDetailsResponse>

    @POST("v2/users/favorites/addOrRemoveFavoriteProduct")
    suspend fun addOrRemoveProductToFavorite(
        @Query("product_id") productId: Int,
        @Query("combination_id") combinationId: Int?
    ): WrappedResponse<AddToFavoriteResponse>

    @POST("v2/users/carts/add")
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

    @GET("v2/products/getProducts")
    suspend fun getProducts(
        @Query("categoryId") type: Int?,
        @Query("page") page: Int?,
        @Query("search") search: String?,
        @Query("tag") tag: Int?,
        @Query("promo") promo: Int?,
        @Query("recommended") recommended: Int?
    ): WrappedListResponse<ProductEntity>


    @GET("v2/users/favorites/getFavoriteProducts")
    suspend fun getFavoriteProducts(@Query("updated") updated: Long): WrappedListResponse<ProductEntity>

}

