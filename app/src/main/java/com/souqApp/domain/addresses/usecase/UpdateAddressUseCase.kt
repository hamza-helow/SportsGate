package com.souqApp.domain.addresses.usecase

import com.souqApp.data.addresses.remote.dto.AddressRequest
import com.souqApp.domain.addresses.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {

    suspend fun invoke(addressRequest: AddressRequest): Flow<Boolean> {
        return flow {
            val response = addressRepository.update(addressRequest)
            emit(response.status)
        }
    }

}