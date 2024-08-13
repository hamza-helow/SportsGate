package com.souqApp.data.users

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.common.utlis.handleApi
import com.souqApp.data.users.remote.api.UsersApi
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.users.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class UsersRepositoryImpl @Inject constructor(private val usersApi: UsersApi) :
    UsersRepository {
    override suspend fun updateUser(
        name: String,
        image: String
    ): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {

        return flow {

            val imageRequestBody =
                File(image).asRequestBody("application/octet-stream".toMediaTypeOrNull())

            val body: RequestBody =
                MultipartBody
                    .Builder()
                    .setType(MultipartBody.FORM).apply {
                        if (image.isNotEmpty()) {
                            addFormDataPart("image", "file", imageRequestBody)
                        }
                        addFormDataPart("name", name)

                    }.build()

            val response = handleApi { usersApi.updateUser(body) }

            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }

        }
    }

    override suspend fun deleteUser(email: String): Flow<BaseResult<List<Any>, WrappedListResponse<Any>>> {
        return flow {
            val response = handleApi { usersApi.deleteUser(email) }
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty(), message = response.message))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun sendOtpByPhone(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response = handleApi { usersApi.sendOtpByPhone() }
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else
                emit(BaseResult.Errors(response))
        }
    }

    override suspend fun sendOtpByEmail(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response = handleApi { usersApi.sendOtpByEmail() }
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else
                emit(BaseResult.Errors(response))
        }
    }

    override suspend fun verifyMobileNumber(code: String): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {

        return flow {
            val response = handleApi { usersApi.verifyMobile(code) }
            if (response.status) {
                val body = response.data
                emit(BaseResult.Success(body.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun verifyEmail(code: String): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return flow {
            val response = handleApi { usersApi.verifyEmail(code) }
            if (response.status) {
                val body = response.data
                emit(BaseResult.Success(body.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}