package com.souqApp.data.profile

import android.util.Log
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.profile.remote.api.ProfileApi
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import okhttp3.MultipartBody


class ProfileRepositoryImpl @Inject constructor(private val profileApi: ProfileApi) :
    ProfileRepository {
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
                        if(image.isNotEmpty())
                        addFormDataPart("image", "file", imageRequestBody)
                        addFormDataPart("name", name)

                    }.build()

            val response = profileApi.updateUser(body)

            Log.e("ERe", "finish")
            Log.e("ERe", "${response.code()}")


            val isSuccessful = response.body()?.status ?: false

            if (isSuccessful) {

                val data = response.body()!!.data!!
                emit(BaseResult.Success(data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }
}