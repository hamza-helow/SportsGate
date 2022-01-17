package com.souqApp.data.common.mapper

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.domain.common.entity.UserEntity


fun UserResponse.toEntity() = UserEntity(id, name, email, phone, image, verified, token)


fun UserEntity.toUserResponse() = UserResponse(id, name, email, phone, image, verified, token?:"")