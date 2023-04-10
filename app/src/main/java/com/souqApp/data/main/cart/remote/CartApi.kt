package com.souqApp.data.main.cart.remote

import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.checkout_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CartApi {

    @GET("v2/users/carts/getCartDetails")
    suspend fun getCartDetails(): Response<WrappedResponse<CartDetailsResponse>>

    @POST("v2/users/carts/deleteProductFromCart")
    suspend fun deleteProductFromCart(@Query("product_id") productId: Int): Response<WrappedResponse<Nothing>>

    @POST("v2/users/carts/updateProductQty")
    suspend fun updateProductQty(
        @Query("product_id") productId: Int,
        @Query("qty") qty: Int
    ): Response<WrappedResponse<UpdateProductQtyResponse>>

    @GET("v2/users/carts/getCheckoutDetails")
    suspend fun getCheckoutDetails(@Query("delivery_option_id") deliveryOptionId: Int?): Response<WrappedResponse<CheckoutDetailsResponse>>

    @POST("v2/users/carts/checkout")
    suspend fun checkout(
        @Query("coupon_code") couponCode: String?,
        @Query("address_id") addressId: Int?,
        @Query("delivery_option_id") deliveryOptionId: Int?
    ): Response<WrappedResponse<CheckoutResponse>>

    @GET("v2/users/carts/checkCouponCode")
    suspend fun checkCouponCode(@Query("coupon_code") couponCode: String): Response<WrappedResponse<Nothing>>


}