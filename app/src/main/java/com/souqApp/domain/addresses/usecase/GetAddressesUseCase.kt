package com.souqApp.domain.addresses.usecase

import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.addresses.AddressRepository
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(private val addressRepository: AddressRepository) {

    suspend fun invoke(): Flow<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>> {
        return flow {
            val response = addressRepository.getAll()
            if (response.status) {
                val data = response.data.orEmpty().toEntity()
                emit(BaseResult.Success(data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}