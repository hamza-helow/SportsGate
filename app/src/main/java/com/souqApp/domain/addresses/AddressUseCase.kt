package com.souqApp.domain.addresses

import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class AddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {

    suspend fun getAll(): Flow<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>> {
        return addressRepository.getAll()
    }

    suspend fun add(addressRequest: AddressRequest): Flow<Boolean> {
        return addressRepository.add(addressRequest)
    }

    suspend fun update(addressRequest: AddressRequest): Flow<Boolean> {

        return addressRepository.update(addressRequest)
    }

    suspend fun delete(addressId: Int): Flow<Boolean> {
        return addressRepository.delete(addressId)
    }

}