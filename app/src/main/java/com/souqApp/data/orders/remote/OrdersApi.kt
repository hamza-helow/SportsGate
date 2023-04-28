package com.souqApp.data.orders.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OrdersApi {
    @GET("v2/users/orders/getOrders")
    suspend fun getOrders(): WrappedListResponse<OrderResponse>

    @GET("v2/users/orders/getOrderDetails")
    suspend fun getOrderDetails(@Query("order_id") order_id: Int): WrappedResponse<OrderDetailsResponse>
}
