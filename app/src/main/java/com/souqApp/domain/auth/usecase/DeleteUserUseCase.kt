package com.souqApp.domain.auth.usecase

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.auth.AuthRepository
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(email: String): Flow<BaseResult<List<Any>, WrappedListResponse<Any>>> {
        return flow {
            val response = authRepository.deleteUser(email)
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty(), message = response.message))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}