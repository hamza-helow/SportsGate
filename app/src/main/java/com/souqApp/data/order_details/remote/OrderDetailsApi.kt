package com.souqApp.data.order_details.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderDetailsApi {

    @GET("v1/users/orders/getOrderDetails")
    suspend fun getOrderDetails(@Query("order_id") order_id: Int): Response<WrappedResponse<OrderDetailsEntity>>
}