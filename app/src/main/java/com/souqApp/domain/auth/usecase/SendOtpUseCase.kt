package com.souqApp.domain.auth.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.auth.AuthRepository
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SendOtpUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(byPhone: Boolean): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response =
                if (byPhone) authRepository.sendOtpByPhone() else authRepository.sendOtpByEmail()
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else
                emit(BaseResult.Errors(response))
        }
    }
}