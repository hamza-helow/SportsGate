package com.souqApp.data.product_details.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductDetailsApi {
    @GET("v1/products/getProductDetails")
    suspend fun productDetails(@Query("product_id") productId: Int): Response<WrappedResponse<ProductDetailsEntity>>


    @FormUrlEncoded
    @POST("v1/users/favourites/addOrRemoveProduct")
    suspend fun addOrRemoveProduct(@Field("product_id") productId: Int): Response<WrappedResponse<Nothing>>

    @FormUrlEncoded
    @POST("v1/users/carts/addProductToCart")
    suspend fun addProductToCart(@Field("product_id") productId: Int): Response<WrappedResponse<Nothing>>
}