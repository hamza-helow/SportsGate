package com.souqApp.data.checkout_details.remote

import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.checkout_details.remote.dto.CheckoutDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentDetailsApi {

    @GET("v2/users/carts/getCheckoutDetails")
    suspend fun getCheckoutDetails(@Query("delivery_option_id") deliveryOptionId: Int?): Response<WrappedResponse<CheckoutDetailsResponse>>

    @GET("v2/users/addresses/getAll")
    suspend fun getAddresses(): Response<WrappedListResponse<AddressResponse>>

    @POST("v2/users/carts/checkout")
    suspend fun checkout(
        @Query("coupon_code") couponCode: String?,
        @Query("address_id") addressId: Int?,
        @Query("delivery_option_id") deliveryOptionId: Int?
    ): Response<WrappedResponse<CheckoutResponse>>

    @GET("v2/users/carts/checkCouponCode")
    suspend fun checkCouponCode(@Query("coupon_code") couponCode: String): Response<WrappedResponse<Nothing>>
}