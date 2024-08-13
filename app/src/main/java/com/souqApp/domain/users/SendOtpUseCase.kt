package com.souqApp.domain.users

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SendOtpUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    suspend fun invoke(byPhone: Boolean): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return if (byPhone)
            usersRepository.sendOtpByPhone()
        else
            usersRepository.sendOtpByEmail()
    }
}