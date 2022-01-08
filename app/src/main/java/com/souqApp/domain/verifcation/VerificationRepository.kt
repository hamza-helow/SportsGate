package com.souqApp.domain.verifcation

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface VerificationRepository {

    suspend fun activeAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>>
}