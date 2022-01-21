package com.souqApp.domain.change_password

import com.souqApp.data.change_password.remote.dto.ChangePasswordRequest
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow

interface ChangePasswordRepository {

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>
}