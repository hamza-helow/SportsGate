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

    override suspend fun getAll(): Flow<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>> {
        return flow {

            val response = addressApi.getAll()
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!.toEntity()
                emit(BaseResult.Success(data))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }

    override suspend fun getDetails(addressId: Int): Flow<BaseResult<AddressDetailsEntity, WrappedResponse<AddressDetailsResponse>>> {
        return flow {

            val response = addressApi.getDetails(addressId)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!.toEntity()
                emit(BaseResult.Success(data))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }

    override suspend fun add(addressRequest: AddressRequest): Flow<Boolean> {
        return flow {

            val response = addressApi.add(addressRequest)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                emit(true)

            } else {

                emit(false)
            }

        }
    }

    override suspend fun update(addressRequest: AddressRequest): Flow<Boolean> {
        return flow {

            val response = addressApi.update(addressRequest)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                emit(true)

            } else {

                emit(false)
            }


        }
    }

    override suspend fun delete(addressId: Int): Flow<Boolean> {
        return flow {

            val response = addressApi.delete(addressId)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                emit(true)

            } else {

                emit(false)
            }
        }
    }

    override suspend fun getCitiesHaveAreas(): Flow<BaseResult<List<CityEntity>, WrappedListResponse<CityResponse>>> {
        return flow {

            val response = addressApi.getCitiesHaveAreas()
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {

                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }


        }
    }

    override suspend fun changeDefault(addressId: Int): Flow<Boolean> {
        return flow {
            val response = addressApi.changeDefault(addressId)
            val isSuccessful = response.body()?.status
            if (isSuccessful == true) {
                emit(true)

            } else {

                emit(false)
            }
        }
    }
}