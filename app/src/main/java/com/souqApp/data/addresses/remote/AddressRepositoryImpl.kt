package com.souqApp.data.addresses.remote

import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.addresses.AddressRepository
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val addressApi: AddressApi) :
    AddressRepository {

    override suspend fun getAll(): WrappedListResponse<AddressResponse> {
        return addressApi.getAll()

    }

    override suspend fun getDetails(addressId: Int): WrappedResponse<AddressDetailsResponse> {
        return addressApi.getDetails(addressId)
    }

    override suspend fun add(addressRequest: AddressRequest): WrappedResponse<Nothing> {
        return addressApi.add(addressRequest)
    }

    override suspend fun update(addressRequest: AddressRequest): WrappedResponse<Nothing> {
        return addressApi.update(addressRequest)
    }

    override suspend fun delete(addressId: Int): WrappedResponse<Nothing> {
        return addressApi.delete(addressId)
    }

    override suspend fun getCitiesHaveAreas(): WrappedListResponse<CityResponse> {
        return addressApi.getCitiesHaveAreas()

    }

    override suspend fun changeDefault(addressId: Int): WrappedResponse<Nothing> {
        return addressApi.changeDefault(addressId)
    }
}