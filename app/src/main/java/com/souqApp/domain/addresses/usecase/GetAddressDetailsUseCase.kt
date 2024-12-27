package com.souqApp.domain.addresses.usecase

import com.souqApp.data.addresses.remote.dto.AddressDetailsResponse
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.addresses.AddressDetailsEntity
import com.souqApp.domain.addresses.AddressRepository
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAddressDetailsUseCase @Inject constructor(private val addressRepository: AddressRepository) {

    suspend fun invoke(addressId: Int): Flow<BaseResult<AddressDetailsEntity, WrappedResponse<AddressDetailsResponse>>> {
        return flow {
            val response = addressRepository.getDetails(addressId)
            if (response.status) {
                val data = response.data.toEntity()
                emit(BaseResult.Success(data))
            } else {
                emit(BaseResult.Errors(response))
            }

        }
    }
}