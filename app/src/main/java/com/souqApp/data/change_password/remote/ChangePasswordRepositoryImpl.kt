package com.souqApp.data.change_password.remote

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.change_password.ChangePasswordRepository
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePasswordRepositoryImpl @Inject constructor(private val changePasswordApi: ChangePasswordApi) :
    ChangePasswordRepository {
    override suspend fun changePassword(oldPassword:String , newPassword:String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {

        return flow {

            val response = changePasswordApi.changePassword(oldPassword, newPassword)

            val isSuccessful = response.status

            if (isSuccessful) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {

                emit(BaseResult.Errors(response))

            }
        }
    }


}