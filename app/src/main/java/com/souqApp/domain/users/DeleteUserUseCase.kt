package com.souqApp.domain.users

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    suspend fun invoke(email: String): Flow<BaseResult<List<Any>, WrappedListResponse<Any>>> {
        return usersRepository.deleteUser(email)
    }

}