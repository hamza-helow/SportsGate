package com.souqApp.data.change_password.remote

import com.souqApp.data.change_password.remote.dto.ChangePasswordRequest
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.change_password.ChangePasswordRepository
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePasswordRepositoryImpl @Inject constructor(private val changePasswordApi: ChangePasswordApi) :
    ChangePasswordRepository {
    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {

        return flow {

            val response = changePasswordApi.changePassword(changePasswordRequest)

            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {

                emit(BaseResult.Errors(response.body()!!))

            }

        }
    }


}