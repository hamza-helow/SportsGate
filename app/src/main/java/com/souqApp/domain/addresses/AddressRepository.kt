package com.souqApp.domain.addresses

import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AddressRepository {

    suspend fun getAll(): WrappedListResponse<AddressResponse>

    suspend fun getDetails(addressId: Int): WrappedResponse<AddressDetailsResponse>

    suspend fun add(addressRequest: AddressRequest): WrappedResponse<Nothing>

    suspend fun update(addressRequest: AddressRequest): WrappedResponse<Nothing>

    suspend fun delete(addressId: Int): WrappedResponse<Nothing>

    suspend fun getCitiesHaveAreas(): WrappedListResponse<CityResponse>

    suspend fun changeDefault(addressId: Int): WrappedResponse<Nothing>

}