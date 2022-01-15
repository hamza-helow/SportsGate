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

        val imgFile = File(image)
        val requestFile = imgFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())


        val builder: MultipartBody.Builder = MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("image", imgFile.name, requestFile);
        builder.addFormDataPart("name", "hamza");

        val imgPart: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", imgFile.name, requestFile)

        val namePart: MultipartBody.Part =
            MultipartBody.Part.createFormData("name", "hamza")


        return flow {

            Log.e("ERe", "upload ......")
            val response = profileApi.updateUser(imgPart, namePart)

            Log.e("ERe", "${response.code()}")

            Log.e("ERe", "ererer")

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