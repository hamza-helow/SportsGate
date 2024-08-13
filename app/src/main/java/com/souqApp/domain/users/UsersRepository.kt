package com.souqApp.domain.users

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    suspend fun updateUser(
        name: String,
        image: String
    ): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>>

    suspend fun deleteUser(email: String): Flow<BaseResult<List<Any>, WrappedListResponse<Any>>>

    suspend fun sendOtpByPhone(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>

    suspend fun sendOtpByEmail(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>

    suspend fun verifyMobileNumber(code: String): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>>

    suspend fun verifyEmail(code: String): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>>
}