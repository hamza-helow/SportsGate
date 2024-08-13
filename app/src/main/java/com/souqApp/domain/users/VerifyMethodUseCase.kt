package com.souqApp.domain.users

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyMethodUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    suspend fun invoke(
        byMobile: Boolean,
        code: String
    ): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return if (byMobile)
            usersRepository.verifyMobileNumber(code)
        else
            usersRepository.verifyEmail(code)
    }

}