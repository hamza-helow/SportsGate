package com.souqApp.domain.profile

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun updateUser(
        name: String,
        image: String
    ): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>>
}