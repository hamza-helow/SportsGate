package com.souqApp.data.addresses.remote

import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AddressApi {

    @GET("v2/users/addresses/getUsersAddresses")
    suspend fun getAll(): WrappedListResponse<AddressResponse>

    @GET("v2/users/addresses/getAddressDetails")
    suspend fun getDetails(@Query("address_id") addressId: Int): WrappedResponse<AddressDetailsResponse>

    @POST("v2/users/addresses/create")
    suspend fun add(@Body addressRequest: AddressRequest): WrappedResponse<Nothing>

    @POST("v2/users/addresses/update")
    suspend fun update(@Body addressRequest: AddressRequest): WrappedResponse<Nothing>

    @POST("v2/users/addresses/delete")
    suspend fun delete(@Query("address_id") addressId: Int): WrappedResponse<Nothing>

    @GET("v1/cities/getCitiesHaveAreas")
    suspend fun getCitiesHaveAreas(): WrappedListResponse<CityResponse>

    @POST("v1/users/addresses/changeDefault")
    suspend fun changeDefault(@Query("address_id") addressId: Int): WrappedResponse<Nothing>
}