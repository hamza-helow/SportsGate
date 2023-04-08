package com.souqApp.data.product_details.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductDetailsApi {
    @GET("v2/products/getProductDetails")
    suspend fun productDetails(@Query("id") productId: Int): Response<WrappedResponse<ProductDetailsEntity>>

    @FormUrlEncoded
    @POST("v2/users/favourites/addOrRemoveProduct")
    suspend fun addOrRemoveProduct(@Field("product_id") productId: Int): Response<WrappedResponse<Nothing>>

    @FormUrlEncoded
    @POST("v2/users/carts/addProductToCart")
    suspend fun addProductToCart(
        @Field("product_id") productId: Int,
        @Field("combination_id") combinationId:Int?,
        @Field("qty") count: Int = 1
    ): Response<WrappedResponse<Nothing>>

    @GET("v2/products/getVariationProductPriceInfo")
    suspend fun getVariationProductPriceInfo(
        @Query("id") productId: Int,
        @Query("label") label: String
    ): Response<WrappedResponse<VariationProductPriceInfoResponse>>

}