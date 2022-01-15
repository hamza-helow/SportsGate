package com.souqApp.domain.profile

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend fun updateProfile(
        name: String,
        image: String
    ): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return profileRepository.updateUser(name, image)
    }

}