package com.souqApp.data.addresses.remote

import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AddressApi {

    @GET("v1/users/addresses/getAll")
    suspend fun getAll(): Response<WrappedListResponse<AddressResponse>>

    @POST("v1/users/addresses/add")
    suspend fun add(@Body addressRequest: AddressRequest): Response<WrappedListResponse<Nothing>>

    @POST("v1/users/addresses/update")
    suspend fun update(@Body addressRequest: AddressRequest): Response<WrappedListResponse<Nothing>>

    @POST("v1/users/addresses/delete")
    suspend fun delete(@Query("address_id") addressId: Int): Response<WrappedListResponse<Nothing>>
}