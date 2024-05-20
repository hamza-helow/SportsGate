package com.souqApp.data.main.cart.remote

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CartApi {

    @GET("v2/users/carts/details")
    suspend fun getCartDetails(): WrappedResponse<CartDetailsResponse>

    @DELETE("v2/users/carts/remove")
    suspend fun deleteProductFromCart(@Query("id") cartItemId: Int): WrappedResponse<UpdateProductCartResponse>

    @POST("v2/users/carts/updateQty")
    suspend fun updateProductQty(
        @Query("cart_id") productId: Int,
        @Query("qty") qty: Int,
        @Query("combination_id") combinationId: Int?,
    ): WrappedResponse<UpdateProductCartResponse>

    @GET("v2/users/carts/checkoutDetails")
    suspend fun getCheckoutDetails(@Query("delivery_option_id") deliveryOptionId: Int?): WrappedResponse<CheckoutDetailsResponse>

    @POST("v2/users/carts/checkout")
    suspend fun checkout(
        @Query("coupon_code") couponCode: String?,
        @Query("address_id") addressId: Int?,
        @Query("delivery_option_id") deliveryOptionId: Int?
    ): WrappedResponse<CheckoutResponse>

    @GET("v2/users/carts/checkCouponCode")
    suspend fun checkCouponCode(@Query("coupon_code") couponCode: String): WrappedResponse<Nothing>


    @DELETE("v2/users/carts/reset")
    suspend fun resetCart(): WrappedResponse<Nothing>

}