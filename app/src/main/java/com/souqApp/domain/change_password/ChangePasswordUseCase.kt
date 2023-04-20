package com.souqApp.domain.change_password

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val changePasswordRepository: ChangePasswordRepository) {

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return changePasswordRepository.changePassword(oldPassword, newPassword)
    }
}
