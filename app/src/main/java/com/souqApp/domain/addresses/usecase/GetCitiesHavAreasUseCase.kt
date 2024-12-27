package com.souqApp.domain.addresses.usecase

import com.souqApp.data.addresses.remote.dto.CityResponse
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.addresses.AddressRepository
import com.souqApp.domain.addresses.CityEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCitiesHavAreasUseCase @Inject constructor(private val addressRepository: AddressRepository) {

    suspend fun invoke(): Flow<BaseResult<List<CityEntity>, WrappedListResponse<CityResponse>>> {
        return flow {
            val response = addressRepository.getCitiesHaveAreas()
            if (response.status) {
                emit(BaseResult.Success(data = response.data.orEmpty().toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}