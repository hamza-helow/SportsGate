package com.souqApp.domain.addresses.usecase

import com.souqApp.domain.addresses.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {

    suspend fun invoke(addressId: Int): Flow<Boolean> {
        return flow {
            val response = addressRepository.delete(addressId)
            emit(response.status)
        }
    }

}