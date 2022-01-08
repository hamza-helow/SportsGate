package com.souqApp.data.verifcation

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.VerificationApi
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.verifcation.VerificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(private val verificationApi: VerificationApi) :
    VerificationRepository {
    override suspend fun activeAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return flow { }
    }
}