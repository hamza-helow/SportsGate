package com.souqApp.data.main.cart.remote

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CartApi {

    @GET("v1/users/carts/getCartDetails")
    suspend fun getCartDetails(): Response<WrappedResponse<CartDetailsResponse>>

    suspend fun getCheckoutDetails(): Response<WrappedResponse<CheckoutDetailsResponse>>

    suspend fun checkout(@Body checkoutRequest: CheckoutRequest): Response<WrappedResponse<CheckoutResponse>>

    @POST("v1/users/carts/deleteProductFromCart")
    suspend fun deleteProductFromCart(@Query("product_id") productId: Int): Response<WrappedResponse<Nothing>>

    @POST("v1/users/carts/updateProductQty")
    suspend fun updateProductQty(
        @Query("product_id") productId: Int,
        @Query("qty") qty: Int
    ): Response<WrappedResponse<Nothing>>


    @GET("v1/users/carts/checkCouponCode")
    suspend fun checkCouponCode(@Query("coupon_code") couponCode: String): Response<WrappedResponse<Nothing>>

}