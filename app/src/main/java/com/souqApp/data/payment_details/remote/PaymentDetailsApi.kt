package com.souqApp.data.payment_details.remote

import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.payment_details.remote.dto.CheckoutDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentDetailsApi {

    @GET("v1/users/carts/getCheckoutDetails")
    suspend fun getCheckoutDetails(): Response<WrappedResponse<CheckoutDetailsResponse>>

    @GET("v1/users/addresses/getAll")
    suspend fun getAddresses(): Response<WrappedListResponse<AddressResponse>>


    @POST("v1/users/carts/checkout")
    suspend fun checkout(@Body checkoutRequest: CheckoutRequest): Response<WrappedResponse<CheckoutResponse>>

    @GET("v1/users/carts/checkCouponCode")
    suspend fun checkCouponCode(@Query("coupon_code") couponCode: String): Response<WrappedResponse<Nothing>>
}