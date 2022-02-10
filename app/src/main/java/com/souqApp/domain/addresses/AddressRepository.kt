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

    suspend fun getAll(): Flow<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>>

    suspend fun getDetails(addressId: Int): Flow<BaseResult<AddressDetailsEntity, WrappedResponse<AddressDetailsResponse>>>

    suspend fun add(addressRequest: AddressRequest): Flow<Boolean>

    suspend fun update(addressRequest: AddressRequest): Flow<Boolean>

    suspend fun delete(addressId: Int): Flow<Boolean>

    suspend fun getCitiesHaveAreas(): Flow<BaseResult<List<CityEntity>, WrappedListResponse<CityResponse>>>

    suspend fun changeDefault(addressId: Int): Flow<Boolean>


}